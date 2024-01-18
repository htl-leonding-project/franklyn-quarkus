import { render, html } from "lit-html";
import { Exam, Model, User, store } from "../model";
import { distinctUntilChanged, map } from "rxjs";

interface ExamViewModel {
  admin: number;
  selectedExam: Exam;
}

const examTemplate = (vm: ExamViewModel) => {
  console.log("exam Template");
  console.log(vm.admin);
  return html`
    <link rel="stylesheet" href="../../styles/style.css" />

    <h2>Test erstellen</h2>

    <form id="examForm">
      <label for="title">Titel:</label>
      <input type="text" id="title" name="title" required /><br />

      <label for="state">Status:</label>
      <select id="state" name="state" required>
        <option value="IN_PREPARATION">In Vorbereitung</option>
        <option value="RUNNING">Aktuell laufen</option></select
      ><br />

      <label for="date">Datum:</label>
      <input type="date" id="date" name="date" required /><br />

      <label for="startTime">Beginnt um</label>
      <input type="time" id="startTime" name="startTime" required /><br />

      <label for="endTime">Endet um</label>
      <input type="time" id="endTime" name="endTime" required /><br />

      <div id="flex-container2">
        <label for="interval">Bilder pro Sekunde:</label>
        <span id="intervalValue">0</span>
      </div>
        <input type="range" id="slider" name="slider" min="1" max="30" step="1" required />
      <br>
      
      <button type="button">Test erstellen</button>
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

        const createButton = this.shadowRoot.querySelector("button");
        createButton.addEventListener("click", () => this.createExam(params));

        const slider = this.shadowRoot.querySelector("#slider");
        slider.addEventListener("input", () => this.updateIntervalDisplay());

        this.updateIntervalDisplay();
      });
  }

  extractParams(route) {
    const routeParts = route.split("/");
    return {
      id: routeParts[routeParts.length - 1], // Annahme: ID ist der letzte Teil der Route
    };
  }

  updateIntervalDisplay() {
    const slider = this.shadowRoot.querySelector("#slider");
    if (slider instanceof HTMLInputElement) {
      const sliderValue = slider.value;
      const intervalDisplay = this.shadowRoot.querySelector("#intervalValue");
      if (intervalDisplay) {
        intervalDisplay.textContent = sliderValue;
      }
    }
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
