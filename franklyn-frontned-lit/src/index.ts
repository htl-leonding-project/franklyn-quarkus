import {enableMapSet, produce} from "immer";
import {Model, store} from "./model";
import {userService} from "./user-service";
import "./components/user-table-component";
import "./components/exam-component";
import "./components/all-sessions-component";
import "./components/session-component"
import userSessionService from "./user-session-service";

enableMapSet()
const users = await userService.getAll();
const admin = 1;
const sessions = await userSessionService.getSessionByExamId(1);
const imageOfEachStudent = new Map<number, string>()

sessions.forEach(session => imageOfEachStudent.set(session.user.id, session.ip))


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
    model.imagesOfStudents = imageOfEachStudent
    return model;
});
store.next(nextState);


