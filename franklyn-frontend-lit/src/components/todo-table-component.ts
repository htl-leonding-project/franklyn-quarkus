import { render, html } from "lit-html";
import { Model, ToDo, User, store } from "../model";
import { distinctUntilChanged, map } from "rxjs";

interface ViewModel {
  zutun: ToDo[];
  nachricht: string;
  users: User[];
}

function toDoClicked(todo: ToDo) {
  alert(`todo ${todo.title} clicked`);
}

const rowTemplate = (todo: ToDo) => html`
  <tr id=${`tr-${todo.id}`} @click=${() => toDoClicked(todo)}>
    <td>${todo.id}</td>
    <td>${todo.title}</td>
  </tr>
`;
const tableTemplate = (vm: ViewModel) => {
  const rows = vm.zutun.map((todo) => rowTemplate(todo));
  return html`
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css" />
    <table class="w3-table-all">
      <caption>
        ${vm.nachricht}
      </caption>
      <thead>
        <tr>
          <th>Id</th>
          <th>Title</th>
        </tr>
      </thead>
      <tbody>
        ${rows}
      </tbody>
    </table>
  `;
};

function toViewModel(model: Model) {
  const vm: ViewModel = {
    zutun: model.todos,
    nachricht: model.message,
    users: model.users,
  };
  return vm;
}

class TodoTableComponent extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }
  async connectedCallback() {
    console.log("connected");

    store
      .pipe(
        distinctUntilChanged(undefined, (model) => model.todos),
        // distinctUntilChanged(undefined, (model) => model.users),
        map(toViewModel)
      )
      .subscribe((viewModel) =>
        render(tableTemplate(viewModel), this.shadowRoot)
      );
  }
}
customElements.define("todo-table", TodoTableComponent);
