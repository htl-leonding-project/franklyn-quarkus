import { produce } from "immer";
import { Model, store } from "./model";
import { userService } from "./user-service";
import "./components/user-table-component";
import "./components/exam-component";
import userSessionService from "./user-session-service";
import "./components/exam-component";

const users = await userService.getAll();
const admin = 1;
const sessions = await userSessionService.getSessionByExamId(1);
// TODO: 1 muss ausgetauscht werden -> dynamisch übergeben
/*
const nextState: Model = {
    todos,
    message
}
*/
const nextState = produce(store.getValue(), (model) => {
  model.users = users;
  model.admin = admin;
  model.sessions = sessions;
});
store.next(nextState);


