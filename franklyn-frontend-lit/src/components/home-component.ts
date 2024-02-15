import { render, html } from "lit-html";

const homeTemplate = () => {
  return html`
    <link rel="stylesheet" type="text/css" href="/styles/style.css" />

    <div id="heading">
      <h1>Franklyn</h1>
      <p>PRÜFUNGSSYSTEM</p>
    </div>

    <div id="about">
      <h2>Features</h2>
      <div id="flex-container">
        <div id="flex-column">
          <img src="/img/cloud.png" />
          <h3>Netzwerkzugang</h3>
          <p>Direkter Online-Zugriff für Recherche und Hilfsmittel</p>
        </div>
        <div id="flex-column">
          <img src="/img/analyse.png" />
          <h3>Realitätsnah</h3>
          <p>Online-Lösungssuche, spiegelt reale Arbeitssituationen wider</p>
        </div>
        <div id="flex-column">
          <img src="/img/chancengleichheit.png" />
          <h3>Echtzeit-Feedback und -Unterstützung</h3>
          <p>Lehrkräfte können die Schritte ihrer Schüler live verfolgen</p>
        </div>
      </div>

      <div id="explainFranklyn">
        <h2>Was ist Franklyn?</h2>
        <p>
          Franklyn ist ein Tool, das eine realitätsnahe Prüfungssituation
          schafft, indem es Bildschirminhalte dokumentiert und diese dem Host
          (z.B. Lehrkraft) präsentiert. Dies ermöglicht den Schülern,
          Internetressourcen während eines Tests zu nutzen.
        </p>
      </div>
    </div>
  `;
};

class FranklynHomeComponent extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }
  async connectedCallback() {
    render(homeTemplate(), this.shadowRoot);
  }
}
customElements.define("franklyn-home", FranklynHomeComponent);
