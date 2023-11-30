import { BehaviorSubject } from "rxjs";

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

export interface Model {
  readonly users: User[];
  readonly message: string;
  readonly admin: number;
}

const initialState: Model = {
  users: [],
  admin: 1,
  message: "lassen wir die Pause ausfallen und hören 5 Minuten früher auf",
};

const store = new BehaviorSubject<Model>(initialState);

export { store };
