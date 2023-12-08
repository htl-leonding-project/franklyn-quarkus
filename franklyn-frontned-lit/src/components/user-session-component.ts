import {render, html} from "lit-html";
import {Model, store, UserSession} from "../model";
import {distinctUntilChanged, first, map} from "rxjs";
import {webSocket, WebSocketSubject} from "rxjs/webSocket";

interface SessionViewModel {
    sessions: UserSession[];
    selectUser: (session: UserSession) => void
    selectedIp: string
}


const sessionTemplate = (vm: SessionViewModel) => {
    const sessions = vm.sessions.map(value => session(vm, value))
    return html`
        <link rel="stylesheet" href="../../styles/style.css" />
        
        <div id="responsive">
            ${sessions}
            ${vm.selectedIp}
        </div>`;
}


const session = (vm: SessionViewModel, session: UserSession) => {
    console.info(session)
    return html`
        <div class="gallery">
            <img src="../../placeholger-image.png" alt="Screenshots">

            <button @click=${() => {
                vm.selectUser(session)
            }}>
                <a>${session.user.lastName} ${session.user.firstName}</a>
            </button>
            <br>
        </div>
    `
}

function toViewModel(model: Model) {
    const vm: SessionViewModel = {
        sessions: model.sessions,
        selectUser: (session: UserSession) => {
            store.next({...store.value, selectedIp: session.ip})
            console.log("user selected")
            const socket: WebSocketSubject<any> = webSocket("ws://localhost:8081/image")
           const socketSubscription = socket.asObservable().subscribe(
                (image: Blob) => {
                    console.log("blob received")
                    console.log(image)
                },
                error => console.log,
               () =>  {}

            )

            socketSubscription.add(socket.pipe(first()).subscribe(() => {
                console.log('WebSocket opened, sending message');
                socket.next("hi");
            }));



        },
        selectedIp: store.value.selectedIp

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
                distinctUntilChanged(undefined, (model) => model),
                map(toViewModel)
            )
            .subscribe((viewModel) =>
                render(sessionTemplate(viewModel), this.shadowRoot)
            );
    }
}

customElements.define("session-view", SessionComponent);
