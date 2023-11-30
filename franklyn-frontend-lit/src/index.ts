import { produce } from "immer";
import { Model, store } from "./model";
import { todoService } from "./todo-service";
import { userService } from "./user-service";
import "./components/todo-table-component";
import "./components/user-table-component";

const todos = await todoService.getAll();
const users = await userService.getAll();
/*
const nextState: Model = {
    todos,
    message
}
*/
const nextState = produce(store.getValue(), (model) => {
  model.todos = todos;
  model.users = users;
});
store.next(nextState);
