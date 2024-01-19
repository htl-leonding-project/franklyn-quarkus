import {Model, store, UserSession} from "../model";
import {WebSocketSubject} from "rxjs/webSocket";
import {html, render} from "lit-html";
import {catchError, interval, switchMap} from "rxjs";
import {produce} from "immer";
import frameService from "../frame-service";

interface ViewModel {
    session: UserSession
    currentImage: string
}

const userSession = (vm: ViewModel) => {
    console.log(vm.currentImage)
    return html`
        <link rel="stylesheet" href="../../styles/style.css""/>

        <a target="_blank" href="/${vm.session.user.id}">
            <div class="gallery">
                <img id="screenshot" width="200px" src=${"data:image/png;base64," + vm.currentImage}
                     alt="Screenshots">
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
    static observedAttributes = ["user-id", "exam-name", "user-name"];

    #session: UserSession
    #webSocket: WebSocketSubject<{ message: string }>;
    #id: number
    #userName: string
    #examName: string


    constructor() {
        super();

        this.attachShadow({mode: "open"})

    }

    attributeChangedCallback(name: string, __: string, newValue: string) {
        this.#id = name === "user-id" ? Number(newValue) : this.#id;
        this.#examName = name === "exam-name" ? newValue : this.#examName
        this.#userName = name === "user-name" ? newValue : this.#userName

    }

    connectedCallback() {
        interval(3000)
            .pipe(
                switchMap(async () => await frameService.getImageForUser(this.#examName, this.#userName)),
                catchError(e => {
                    console.error("Error with fetching data", e.data)
                    return ""
                })
            ).subscribe(data => {
            console.info(data, "structure of data")
            const newModel = produce(store.getValue(), model => {
                model.imagesOfStudents = model.imagesOfStudents.set(this.#id, typeof data === "string" ? "" : data?.image)
                return model;
            })
            store.next(newModel)
            render(userSession({
                session: store.getValue().sessions.find(s => s.user.id === this.#id),
                currentImage: store.getValue().imagesOfStudents.get(this.#id)
            }), this.shadowRoot);
        })

    }

}

customElements.define("user-session-view", UserSessionComponent)

