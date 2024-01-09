import { render, html } from "lit-html";
import { Exam, Model, User, store } from "../model";
import { distinctUntilChanged, map } from "rxjs";

interface ExamViewModel {
  selectedExam: Exam;
}

const tableTemplate = (vm: ExamViewModel) => {
  // const rows = vm.exams.map((exam) => rowTemplate(exam));
  return html`
    <section>
      <h2>${vm.selectedExam.title}</h2>
      <p>${vm.selectedExam.date}</p>
      <p></p>
      <p>${vm.selectedExam.startTime}</p>
      <p></p>
      <p>${vm.selectedExam.endTime}</p>
      <p></p>
      <p>${vm.selectedExam.examState}</p>
      <p></p>
      <p>${vm.selectedExam.interval}</p>
      <p></p>
      <p>${vm.selectedExam.pin}</p>
      <p></p>
      <p>${vm.selectedExam.id}</p>
      <p></p>
    </section>
    <a href="/exam/edit/${vm.selectedExam.id}">Edit</a>
  `;
};

function toViewModel(model: Model) {
  const vm: ExamViewModel = {
    selectedExam: model.selectedExam,
  };
  return vm;
}

class ExamDetailComponent extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }
  async connectedCallback() {
    console.log("connected");

    store
      .pipe(
        // distinctUntilChanged(undefined, (model) => model.todos),
        distinctUntilChanged(undefined, (model) => model.selectedExam),
        map(toViewModel)
      )
      .subscribe((viewModel) =>
        render(tableTemplate(viewModel), this.shadowRoot)
      );
  }
}
customElements.define("exam-detail", ExamDetailComponent);
