import { render, html } from "lit-html";

const navTemplate = () => {
  return html`
    <a href="/">Home</a>
    <a href="/exams">Exam übersicht</a>
    <a href="/users">alle User</a>
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
