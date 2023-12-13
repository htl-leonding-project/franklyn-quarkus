import { BehaviorSubject } from "rxjs";
import * as string_decoder from "string_decoder";

export interface User {
  readonly id: number;
  readonly firstName: string;
  readonly lastName: string;
  readonly emial: string;
  readonly isOnine: boolean;
  readonly isAdmin: boolean;
  readonly lastOnline: Date;
  readonly onine: boolean;
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
  readonly exams: Exam[];
  readonly message: string;
  readonly admin: number;
  readonly sessions: UserSession[];
  selectedIp: string;
  selectedExam: number;
}

export type UserSession = { user: User; ip: string | null };

const initialState: Model = {
  users: [],
  exams: [],
  admin: 1,
  message: "lassen wir die Pause ausfallen und hören 5 Minuten früher auf",
  sessions: [],
  selectedIp: null,
  selectedExam: null,
};

const store = new BehaviorSubject<Model>(initialState);

export { store };
