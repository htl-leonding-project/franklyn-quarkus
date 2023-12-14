import { render, html } from "lit-html";

const homeTemplate = () => {
    return html`
        <link rel="stylesheet" type="text/css" href="../../styles/style.css" /> 
        
        <div id="heading">
            <h1>Franklyn</h1>
            <p>Prüfungssystem</p>
        </div>
        
        <div id="about">
            <h2>Features</h2>
            <div id="flex-container">
                <div id="flex-column">
                    <img src="../../img/cloud.png">
                    <h3>Überall verfügbar</h3>
                    <p>Franklyn läuft auf jeder Plattform und benötigt keine Installation</p>
                </div>
                <div id="flex-column">
                    <img src="../../img/analyse.png">
                    <h3>Analyse</h3>
                    <p>Lehrkräfte können sich den Fortschritt ihrer Schüler live oder im Nachhinein ansehen</p>
                </div>
                <div id="flex-column">
                    <img src="../../img/chancengleichheit.png">
                    <h3>Chancengleichheit</h3>
                    <p>Franklyn läuft auf jeder Plattform und benötigt keine Installation</p>
                </div>
            </div>
            
            <div id="explainFranklyn">
                <h2>Was ist Franklyn?</h2>
                <p>Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum Lorem Ipsum</p>
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
