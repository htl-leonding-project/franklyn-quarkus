import { render, html } from "lit-html";
import { Model, User, store } from "../model";
import { distinctUntilChanged, map } from "rxjs";

interface UserViewModel {
  users: User[];
}

function toDoClicked(user: User) {
  alert(`user: ${user.email} clicked`);
}

const rowTemplate = (user: User) => html`
  <tr id=${`tr-${user.id}`} @click=${() => toDoClicked(user)}>
    <td>${user.id}</td>
    <td>${user.firstName}</td>
    <td>${user.lastName}</td>
    <td>${user.email}</td>
  </tr>
`;
const tableTemplate = (vm: UserViewModel) => {
  const rows = vm.users.map((user) => rowTemplate(user));
  return html`
    <link rel="stylesheet" href="/styles/style.css" />

    <h2>User Tabelle</h2>

    <table>
      <thead>
        <tr>
          <th id="Id">Id</th>
          <th id="Vorname">Vorname</th>
          <th id="Nachname">Nachname</th>
          <th id="Email">E-Mail</th>
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
