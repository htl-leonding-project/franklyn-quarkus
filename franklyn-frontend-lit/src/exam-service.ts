import { Exam } from "./model";

class ExamService {
  #url1 = "/api/exam/all";
  //#url2 = "https://jsonplaceholder.typicode.com/todos";
  async getAll() {
    let exams: Exam[];

    const response = await fetch(this.#url1, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
      },
    });

    //console.log(response.statusText, response.status);
    if (response.ok) {
      exams = await response.json();
    }
    console.log(exams);
    return exams;
  }
}

const examService = new ExamService();
export { examService };
