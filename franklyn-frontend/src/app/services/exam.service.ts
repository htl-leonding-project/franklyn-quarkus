import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Exam } from '../models/exam.model';
import { NewExam } from '../models/new-exam.model';


const httpOptions = {
  headers: new HttpHeaders({
  'Content-Type': 'application/json'
  //,'Authorization': 'my-auth-token'
  })
  }

  
@Injectable({
  providedIn: 'root'
})
export class ExamService {

  constructor(private http: HttpClient) { }

  postNewTest(newExam: NewExam) {
    return this.http.post<NewExam>("http://localhost:8080/api/exams", newExam, httpOptions);
  }

  getAll() {
    return this.http.get<Exam[]>("http://localhost:8080/api/exams/all");
  }

  getById(id: string){
    return this.http.get<Exam>("http://localhost:8080/api/exams/exam/"+id);
  }

  getLatestById(id: string){
    return this.http.get<Exam>("http://localhost:8080/api/exams/exam/examiner/"+id);
  }

  getExamById(id: string){
    return this.http.get<Exam>("http://localhost:8080/api/exams/exam/"+id);
  }

  getExamsByExaminer(id: number){
    return this.http.get<Exam[]>("http://localhost:8080/api/exams/examiner/"+id);
  }

  deleteById(examId: number) {
    return this.http.delete("http://localhost:8080/api/exams/delete/"+examId, httpOptions);
  }

}
