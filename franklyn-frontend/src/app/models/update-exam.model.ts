export interface UpdateExam {
    id: number,
    title: string,
    formIds: string[],
    examinerIds: string[],
    date: string,
    interval: string
}
