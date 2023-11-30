import { render, html } from "lit-html";
import { Model, User, store } from "../model";
import { distinctUntilChanged, map } from "rxjs";

interface UserViewModel {
  users: User[];
}

function toDoClicked(user: User) {
  alert(`user: ${user.emial} clicked`);
}

const rowTemplate = (user: User) => html`
  <tr id=${`tr-${user.id}`} @click=${() => toDoClicked(user)}>
    <td>${user.id}</td>
    <td>${user.firstName}</td>
    <td>${user.lastName}</td>
    <td>${user.emial}</td>
  </tr>
`;
const tableTemplate = (vm: UserViewModel) => {
  const rows = vm.users.map((user) => rowTemplate(user));
  return html`
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css" />
    <table class="w3-table-all">
      <thead>
        <tr>
          <th>Id</th>
          <th>Vorname</th>
          <th>Nachname</th>
          <th>E-Mail</th>
        </tr>
      </thead>
      <tbody>
        ${rows}
      </tbody>
    </table>
  `;
};

function toViewModel(model: Model) {
  const vm: UserViewModel = {
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
        // distinctUntilChanged(undefined, (model) => model.todos),
        distinctUntilChanged(undefined, (model) => model.users),
        map(toViewModel)
      )
      .subscribe((viewModel) =>
        render(tableTemplate(viewModel), this.shadowRoot)
      );
  }
}
customElements.define("user-table", TodoTableComponent);
