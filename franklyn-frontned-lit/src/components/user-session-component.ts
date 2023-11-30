import {html} from "lit-html";
import {Model, store, UserSession} from "../model";
import {distinctUntilChanged, map} from "rxjs";
import {render} from "lit-html/development/lit-html";
interface SessionViewModel{
    sessions: UserSession[];
}
const sessionTemplate = (vm: object) =>{
    return html`<h1>test</h1>`;
}

function toViewModel(model: Model) {
    const vm: SessionViewModel = {
        sessions: model.sessions,
    };
    return vm;
}

class SessionComponent extends HTMLElement{
    constructor() {
        super();
        this.attachShadow({ mode: "open" });
    }
    async connectedCallback() {
        console.log("connected");

        store
            .pipe(
                // distinctUntilChanged(undefined, (model) => model.todos),
                distinctUntilChanged(undefined, (model) => model.sessions),
                map(toViewModel)
            )
            .subscribe((viewModel) =>
                render(sessionTemplate(viewModel), this.shadowRoot)
            );
    }
}
customElements.define("session-view", SessionComponent);
