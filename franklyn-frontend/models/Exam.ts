export interface Exam {
  e_id: string;
  id: number;
  pin: number;
  title: string;
  examState: "IN_PREPARATION" | "READY";
  date: Date;
  startTime: Date;
  endTime: Date;
  interval: number;
  adminId: number | null;
  compression: 0;
  isDeleted: boolean;
}
