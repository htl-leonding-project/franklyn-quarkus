import {render, html} from "lit-html";
import {Model, store, UserSession} from "../model";
import {distinctUntilChanged, map} from "rxjs";

interface SessionViewModel {
    sessions: UserSession[];
}

const sessionTemplate = (vm: SessionViewModel) => {
    console.log(store.getValue().sessions)
    const sessions = vm.sessions.map(value => {
        console.log(value)
        return html`
            <user-session-view user-id=${value.user.id}>
            </user-session-view>`
    })
    return html`
        <link rel="stylesheet" href="../../styles/style.css"/>

        <div id="responsive">
            ${sessions}
        </div>`;
}

/*const session = (vm: SessionViewModel, session: UserSession) => {
    /!*
        getImageFromSocket("localhost")
    *!/
    console.info(session)
    return html`
        <a target="_blank" href="/${session.user.id}">
            <div class="gallery">
                <img src=${vm.currentImage} alt="Screenshots">
                <p>${session.user.lastName} ${session.user.firstName}</p>
                <br>
            </div>
        </a>
    `
}*/

/*function getImageFromSocket(host: string) {
    const url = `ws://${host}:8081/image`
    console.info(url)
    const socket = new WebSocketSubject<{ message: string }>(url);
    console.log(socket)
    socket.subscribe({
        next: msg => {
            console.log(msg)
            const imageOfEachStudent = store.getValue().imagesOfStudents;
            store.next({...store.getValue(), imagesOfStudents: "data:image/png;base64," + msg.message})
        },
        error: err => console.error(err.message),
        complete: () => console.log(("complete"))
    })
    socket.next({message: "hi"})
    console.info("message sent")


}*/

function toViewModel(model: Model) {

    const vm: SessionViewModel = {
        sessions: model.sessions,


    };
    return vm;
}

class SessionComponent extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: "open"});
    }

    async connectedCallback() {
        console.log("connected");

        store
            .pipe(
                // distinctUntilChanged(undefined, (model) => model.todos),
                distinctUntilChanged(undefined, (model) => model.sessions),
                map(toViewModel),
            )
            .subscribe((viewModel) =>
                render(sessionTemplate(viewModel), this.shadowRoot)
            );
    }
}

customElements.define("all-sessions", SessionComponent);
