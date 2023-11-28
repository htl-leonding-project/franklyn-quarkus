import { ToDo } from "./model";

class ToDoService {
  #url = "https://jsonplaceholder.typicode.com/todos";

  async getAll() {
    let todos: ToDo[];
    const response = await fetch(this.#url);
    if (response.ok) {
      todos = await response.json();
    }
    console.log(todos);
    return todos;
  }
}
const todoService = new ToDoService();
export { todoService };
