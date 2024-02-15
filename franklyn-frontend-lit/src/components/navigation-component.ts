import { render, html } from "lit-html";

const navTemplate = () => {
  return html`
    <link rel="stylesheet" type="text/css" href="/styles/style.css" />

    <div id="nav">
      <a href="/"><img src="/img/logo.png" /></a>
      <a href="/exams">Prüfungen</a>
      <a href="/users">Nutzerübersicht</a>
    </div>
  `;
};

class FranklynNavComponent extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: "open" });
  }
  async connectedCallback() {
    render(navTemplate(), this.shadowRoot);
  }
}
customElements.define("franklyn-nav", FranklynNavComponent);
