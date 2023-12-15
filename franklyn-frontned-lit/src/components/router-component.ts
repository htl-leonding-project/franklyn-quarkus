import { html, render } from "lit-html";
import { produce } from "immer";
import { store } from "../model";

class RouterComponent extends HTMLElement {
  #routes = {};
  constructor() {
    super();
    this.attachShadow({ mode: "open" });

    const stylesheetLink = document.createElement('link');
    stylesheetLink.rel = 'stylesheet';
    stylesheetLink.href = '/styles/style.css';
    document.head.appendChild(stylesheetLink);

    this.#routes = {
      "/": () => html`<franklyn-home></franklyn-home>`,
      "/users": () => html`<user-table></user-table>`,
      "/exams": () => html`<exam-table></exam-table>`,
      "/exam/:id": () => html`<all-sessions></all-sessions>`,
      "/exam/edit/:id": (params) =>
        html`<exam-form id="${params.id}"></exam-form>`,
      "/home": () => html`<franklyn-home></franklyn-home>`,
      // Add more routes as needed
    };

    window.addEventListener("popstate", () => this.handleRouteChange());
    this.handleRouteChange();
  }

  handleRouteChange() {
    const route = window.location.pathname;
    const matchedRoute = Object.keys(this.#routes).find((pattern) =>
      this.matchRoute(route, pattern)
    );

    if (matchedRoute) {
      const params = this.extractParams(route, matchedRoute);
      this.renderComponent(this.#routes[matchedRoute](params));
      console.log(store.getValue().exams);
      console.log(params);
    } else {
      // Handle 404 or default route
      this.renderComponent(html`<p>Not Found</p>`);
    }
  }

  matchRoute(route, pattern) {
    const routeParts = route.split("/");
    const patternParts = pattern.split("/");

    if (routeParts.length !== patternParts.length) {
      return false;
    }

    for (let i = 0; i < routeParts.length; i++) {
      if (
        patternParts[i] !== routeParts[i] &&
        !patternParts[i].startsWith(":")
      ) {
        return false;
      }
    }

    return true;
  }

  extractParams(route, pattern) {
    const routeParts = route.split("/");
    const patternParts = pattern.split("/");
    const params = {};

    for (let i = 0; i < routeParts.length; i++) {
      if (patternParts[i].startsWith(":")) {
        const paramName = patternParts[i].slice(1);
        params[paramName] = routeParts[i];
      }
    }

    return params;
  }

  renderComponent(component) {
    render(component, this.shadowRoot);
  }
}

customElements.define("router-container", RouterComponent);
