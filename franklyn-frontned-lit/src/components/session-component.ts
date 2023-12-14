import {Model, store, UserSession} from "../model";
import {webSocket, WebSocketSubject} from "rxjs/webSocket";
import {html, render} from "lit-html";
import {combineLatest, distinctUntilChanged, interval, map, repeat} from "rxjs";
import {produce} from "immer";

interface ViewModel {
    session: UserSession
    currentImage: string
}

const userSession = (vm: ViewModel) => {
    console.info("viewModel-value", vm)
    return html`
        <link rel="stylesheet" href="../../styles/style.css""/>

        <a target="_blank" href="/${vm.session.user.id}">
            <div class="gallery">
                <img src=${"data:image/png;base64," + vm.currentImage} alt="Screenshots">
                <p>${vm.session.user.lastName} ${vm.session.user.firstName}</p>
                <br>
            </div>
        </a>`
}


const toViewModel = (model: Model, id: number) => {
    return {
        session: model.sessions.find(s => s.user.id === id),
        currentImage: model.imagesOfStudents.get(id)
    } as ViewModel
}

class UserSessionComponent extends HTMLElement {
    static observedAttributes = ["user-id"];

    #session: UserSession
    #webSocket: WebSocketSubject<{ message: string }>;
    #id: number


    constructor() {
        super();

        this.attachShadow({mode: "open"})

    }

    attributeChangedCallback(_: string, __: string, newValue: string) {
        this.#id = Number(newValue)
        console.log("id: ", this.#id)
        if (this.#id) {
            this.#session = store.getValue().sessions.find(s => s.user.id === this.#id);
            console.info("session: ", this.#session)
            this.#webSocket = new WebSocketSubject<{
                message: string
            }>(`ws://${this.#session.ip || "localhost"}:8081/image`)
        }

    }

    connectedCallback() {
        interval(1000).subscribe(() => {
            this.#webSocket.next({message: "hi"})
        });
        this.#webSocket.subscribe({
            next: ({message}) => {
                const newModel = produce(store.getValue(), model => {
                    model.imagesOfStudents = model.imagesOfStudents.set(this.#id, message);
                    return model
                })
                console.log(newModel.imagesOfStudents.get(this.#id))
                store.next(newModel);
                render(userSession({
                    session: store.getValue().sessions.find(s => s.user.id === this.#id),
                    currentImage: message
                }), this.shadowRoot)
            }
        })
        this.#webSocket.next({message: "hi"})


        /*   combineLatest([
               this.#webSocket
                   .pipe(
                       repeat({delay: 200}),
                       distinctUntilChanged(undefined, value => value.message)
                   ), store
                   .pipe(
                       distinctUntilChanged(undefined, value => value.message),
                       map(model => toViewModel(model, this.#id))
                   )]
           ).subscribe({
               next: ([message, viewModel]) => {
                   console.log(message)
                   store.next({
                       ...store.getValue(),
                       imagesOfStudents: store
                           .getValue()
                           .imagesOfStudents
                           .set(this.#session.user.id, "data:image/png;base64," + message.message)
                   })
                   render(userSession(viewModel), this.shadowRoot)
               }
           })*/

    }

}

customElements.define("user-session-view", UserSessionComponent)

