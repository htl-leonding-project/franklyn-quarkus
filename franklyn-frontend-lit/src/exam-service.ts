import { Exam } from "./model";

class ExamService {
  #url1 = "/api/exam";

  //#url2 = "https://jsonplaceholder.typicode.com/todos";
  async getAll() {
    let exams: Exam[];

    const response = await fetch(this.#url1+"/all", {
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
  async getById(id:number) {
    const response = await  fetch(this.#url1+"/"+id, {
      method: "GET",
      headers: {
        "Content-type": "application/json",
      }
    })
    if (response.ok) {
      return await response.json() as Exam;
    } else {
      console.log(response.statusText)
      return null;
    }
  }
}

const examService = new ExamService();
export { examService };
