import { html, render } from "lit-html";

declare var process: {
  env: {
    BASE_HREF: string;
  };
};

type ValuesOfTheMap = (params?: { id: number }) => ReturnType<typeof html>;
class RouterComponent extends HTMLElement {
  #routes = new Map<string, ValuesOfTheMap>();
  constructor() {
    const baseHRef = process.env.BASE_HREF;
    if (baseHRef) {
      const base = document.querySelector("base");
      if (base) {
        console.log("base=", baseHRef);
        base.setAttribute("href", baseHRef);
      }
    }

    super();
    this.attachShadow({ mode: "open" });

    const stylesheetLink = document.createElement("link");
    stylesheetLink.rel = "stylesheet";
    stylesheetLink.href = "/styles/style.css";
    this.#routes.get("/home");

    document.head.appendChild(stylesheetLink);

    this.#routes.set(baseHRef, () => html`<franklyn-home></franklyn-home>`);
    this.#routes.set(
      baseHRef + "home",
      () => html`<franklyn-home></franklyn-home>`
    );
    this.#routes.set(
      baseHRef + "users",
      (params?: any) => html`<user-table></user-table>`
    );
    this.#routes.set(
      baseHRef + "exams",
      (params?: any) => html`<exam-table></exam-table>`
    );
    this.#routes.set(
      baseHRef + "exam/:id",
      (params: { id: number }) =>
        html`<all-sessions id="${params.id}"></all-sessions>`
    );
    this.#routes.set(
      baseHRef + "exam/edit/:id",
      (params) => html`<exam-form id="${params.id}"></exam-form>`
    );
    window.addEventListener("popstate", (params?: any) =>
      this.handleRouteChange()
    );
    this.handleRouteChange();
  }

  handleRouteChange() {
    const route = window.location.pathname;
    const mapKeys = Array.from(this.#routes.keys());
    console.log(mapKeys);
    const matchedRoute = mapKeys.find((pattern) => {
      console.log(pattern);
      return this.matchRoute(route, pattern);
    });
    console.log(matchedRoute);

    if (matchedRoute) {
      const params = this.extractParams(route, matchedRoute) as
        | { id: number }
        | {};
      let component: ReturnType<typeof html>;
      if ("id" in params) {
        component = this.#routes.get(matchedRoute)(params);
      } else {
        component = this.#routes.get(matchedRoute)();
      }
      this.renderComponent(component);
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

  renderComponent(component: ReturnType<typeof html>) {
    render(component, this.shadowRoot);
  }
}

customElements.define("router-container", RouterComponent);
