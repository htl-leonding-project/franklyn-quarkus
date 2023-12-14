import {BehaviorSubject} from "rxjs";

export interface User {
    readonly id: number;
    readonly firstName: string;
    readonly lastName: string;
    readonly email: string;
    readonly isOnline: boolean;
    readonly isAdmin: boolean;
    readonly lastOnline: Date;
    readonly online: boolean;
    readonly admin: boolean;
}

export interface Exam {
    readonly e_id: string;
    readonly id: number;
    readonly pin: string;
    readonly title: string;
    readonly examState: string;
    readonly date: string;
    readonly startTime: string;
    readonly endTime: string;
    readonly interval: number;
    readonly adminId: number | null;
    readonly compression: number;
    readonly isDeleted: boolean;
}

export interface Model {
    readonly users: User[];
    readonly message: string;
    readonly admin: number;
    readonly sessions: UserSession[];
    readonly imagesOfStudents: Map<number, string>
    readonly exams: Exam[];
    selectedIp: string;
    selectedExam: number;
}

export type UserSession = { user: User; ip: string | null };

const initialState: Model = {
    users: [],
    admin: 1,
    exams: [],
    message: "lassen wir die Pause ausfallen und hören 5 Minuten früher auf",
    sessions: [],
    imagesOfStudents: new Map<number, string>(),
    selectedIp: null,
    selectedExam: null,

};

const store = new BehaviorSubject<Model>(initialState);

export {store};
