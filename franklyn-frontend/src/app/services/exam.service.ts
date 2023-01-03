import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Exam } from '../models/exam.model';
import { NewExam } from '../models/new-exam.model';
import { UpdateExam } from '../models/update-exam.model';


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
    return this.http.post<string>("http://localhost:8080/api/exams", newExam, httpOptions);
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
    console.log(examId)
    return this.http.delete("http://localhost:8080/api/exams/delete/"+examId, httpOptions);
  }

  updateExam(updatedExam: UpdateExam){
    return this.http.put<string>("http://localhost:8080/api/exams/update/" + updatedExam.id, updatedExam, httpOptions);
  }

}
