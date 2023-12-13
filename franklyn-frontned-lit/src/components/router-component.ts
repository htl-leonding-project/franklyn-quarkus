import { html, render } from "lit-html";

class RouterComponent extends HTMLElement {
  #routes = {};
  constructor() {
    super();
    this.attachShadow({ mode: "open" });

    this.#routes = {
      "/": () => html`<h1>Homepage</h1>`,
      "/users": () => html`<user-table></user-table>`,
      "/exams": () => html`<exam-table></exam-table>`,
      "/exam/:id": () => html`<exam-form></exam-form>`,
      "/exam/edit/:id": (params) =>
        html`<exam-form id="${params.id}"></exam-form>`,
      "/home": () => html`<h1>Homepage</h1>`,
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
