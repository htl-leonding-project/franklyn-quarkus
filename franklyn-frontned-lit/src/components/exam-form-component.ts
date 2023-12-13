import { render, html } from "lit-html";
import { Model, User, store } from "../model";
import { distinctUntilChanged, map } from "rxjs";

interface ExamViewModel {
  admin: number;
  selectedExam: number;
}

const examTemplate = (vm: ExamViewModel) => {
  console.log("exam Template");
  console.log(vm.admin);
  return html`
    <link rel="stylesheet" href="../../styles/style.css" />

    <h2>Test erstellen</h2>

    <form id="examForm">
      <label for="title">Title:</label>
      <input type="text" id="title" name="title" required /><br />

      <label for="state">State:</label>
      <select id="state" name="state" required>
        <option value="IN_PREPARATION">In Vorbereitung</option>
        <option value="RUNNING">Aktuell laufen</option></select
      ><br />

      <label for="date">Date:</label>
      <input type="date" id="date" name="date" required /><br />

      <label for="startTime">Start Time:</label>
      <input type="time" id="startTime" name="startTime" required /><br />

      <label for="endTime">End Time:</label>
      <input type="time" id="endTime" name="endTime" required /><br />

      <label for="interval">Interval:</label>
      <input type="number" id="interval" name="interval" required /><br />

      <button type="button">Create</button>
    </form>
  `;
};

function toViewModel(model: Model) {
  const vm: ExamViewModel = {
    admin: model.admin,
    selectedExam: model.selectedExam,
  };
  return vm;
}

class ExamFormComponent extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }

  connectedCallback() {
    const route = window.location.pathname;
    const params = this.extractParams(route);
    console.log("Exam ID:", params.id);
    // console.log("connected exam");

    store
      .pipe(
        distinctUntilChanged(undefined, (model) => model.admin),
        map(toViewModel)
      )
      .subscribe((viewModel) => {
        render(examTemplate(viewModel), this.shadowRoot);

        // Hier fügen Sie den Event-Listener für den "Create" -Button hinzu
        const createButton = this.shadowRoot.querySelector("button");
        createButton.addEventListener("click", () => this.createExam(params));
      });
  }

  extractParams(route) {
    const routeParts = route.split("/");
    return {
      id: routeParts[routeParts.length - 1], // Annahme: ID ist der letzte Teil der Route
    };
  }

  async createExam(params) {
    // @ts-ignore
    const titleInput = this.shadowRoot.querySelector("#title").value;
    // @ts-ignore
    const stateInput = this.shadowRoot.querySelector("#state").value;
    // @ts-ignore
    const dateInput = this.shadowRoot.querySelector("#date").value;
    // @ts-ignore
    const startTimeInput = this.shadowRoot.querySelector("#startTime").value;
    // @ts-ignore
    const endTimeInput = this.shadowRoot.querySelector("#endTime").value;
    // @ts-ignore
    const intervalInput = this.shadowRoot.querySelector("#interval").value;

    const exam = {
      title: titleInput,
      state: stateInput,
      date: dateInput,
      startTime: dateInput + "T" + startTimeInput,
      endTime: dateInput + "T" + endTimeInput,
      interval: intervalInput,
    };

    console.log(JSON.stringify(exam));
    console.log("Exam ID:", params.id);

    fetch("/api/exam/create", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(exam),
    })
      .then((response) => response.json())
      .then((data) => {
        alert(`Test erstellt - PIN: ${data}`);
      })
      .catch((error) => {
        console.error("Fehler beim Erstellen des Tests:", error);
      });
  }
}
customElements.define("exam-form", ExamFormComponent);
