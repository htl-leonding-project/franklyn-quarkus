export interface Exam {
    pin: string
    title: string,
    ongoing: string,
    date: string,
    startTime: Date,
    forms: string,
    nrOfStudents: string,
    examiners: string,
    id: number,
    isToday: boolean,
    canBeEdited: boolean,
    canBeDeleted: boolean
}
