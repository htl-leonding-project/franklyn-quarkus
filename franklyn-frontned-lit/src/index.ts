import { produce } from "immer";
import { Model, store } from "./model";

import { userService } from "./user-service";
import { examService } from "./exam-service";

import "./components/navigation-component";

import "./components/router-component";
import "./components/user-table-component";
import "./components/exam-form-component";
import "./components/exam-table-component";
import "./components/user-session-component";

import userSessionService from "./user-session-service";

const users = await userService.getAll();
const exams = await examService.getAll();
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
  model.exams = exams;
  model.admin = admin;
  model.sessions = sessions;
});

store.next(nextState);
