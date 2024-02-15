import { Exam } from "./Exam";
import { User } from "./User";

export interface UserSession {
  id: number;
  userRole: string;
  user: User;
  exam: Exam;
}
