import {render, html} from "lit-html";
import {Exam, Model, store, UserSession} from "../model";
import {distinctUntilChanged, map} from "rxjs";
import {examService} from "../exam-service";

interface SessionViewModel {
    sessions: UserSession[];
    examName: string
}

const sessionTemplate = (vm: SessionViewModel) => {
    const sessions = vm.sessions.map(value => {
        return html`
            <user-session-view user-id=${value.user.id} exam-name=${vm.examName}
                               user-name=${value.user.lastName + "" + value.user.firstName}>
            </user-session-view>`
    })
    return html`
        <link rel="stylesheet" href="../../styles/style.css"/>

        <div id="responsive">
            ${sessions}
        </div>`;
}


function toViewModel(model: Model, test: Exam) {
    console.log(test)
    const vm: SessionViewModel = {
        sessions: model.sessions,
        examName: `${test.title}_${test.date.split("-").join("")}`


    };
    return vm;
}

class SessionComponent extends HTMLElement {
    constructor() {
        super();
        this.attachShadow({mode: "open"});
    }

    extractParams(route) {
        const routeParts = route.split("/");
        return {
            id: routeParts[routeParts.length - 1], // Annahme: ID ist der letzte Teil der Route
        };
    }

    async connectedCallback() {
        const route = window.location.pathname;
        const {id} = this.extractParams(route);
        console.log(id)
        const test = await examService.getById(Number(id));

        console.log("connected");

        store
            .pipe(
                // distinctUntilChanged(undefined, (model) => model.todos),
                distinctUntilChanged(undefined, (model) => model.sessions),
                map(model => toViewModel(model, test)),
            )
            .subscribe((viewModel) =>
                render(sessionTemplate(viewModel), this.shadowRoot)
            );
    }
}

customElements.define("all-sessions", SessionComponent);
