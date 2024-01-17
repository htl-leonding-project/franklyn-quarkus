import { render, html } from "lit-html";
import { Exam, Model, User, store } from "../model";
import { distinctUntilChanged, map } from "rxjs";

interface ExamViewModel {
  exams: Exam[];
}

function toDoClicked(exam: Exam) {
  alert(`exam: ${exam.id} clicked`);
}

const rowTemplate = (exam: Exam) => html`
  <tr id=${`tr-${exam.id}`} @click=${() => toDoClicked(exam)}>
    <td>${exam.id}</td>
    <td>${exam.title}</td>
    <td>${exam.examState}</td>
    <td>${exam.pin}</td>
    <td>${exam.interval}</td>
  </tr>
`;
const tableTemplate = (vm: ExamViewModel) => {
  const rows = vm.exams.map((exam) => rowTemplate(exam));
  return html`
    <h2>Exams</h2>

    <link rel="stylesheet" href="../../styles/style.css" />

    <table>
      <thead>
        <tr>
          <th id="Id">Id</th>
          <th id="Title">Titel</th>
          <th id="Status">Status</th>
          <th id="Pin">Pin</th>
          <th id="Interval">Interval</th>
        </tr>
      </thead>
      <tbody>
        ${rows}
      </tbody>
    </table>
  `;
};

function toViewModel(model: Model) {
  const vm: ExamViewModel = {
    exams: model.exams,
  };
  return vm;
}

class ExamTableComponent extends HTMLElement {
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
customElements.define("exam-table", ExamTableComponent);
