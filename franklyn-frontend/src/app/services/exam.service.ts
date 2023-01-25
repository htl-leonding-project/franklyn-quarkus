import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Exam } from '../models/exam.model';
import { NewExam } from '../models/new-exam.model';
import { UpdateExam } from '../models/update-exam.model';
import {environment} from "../../environments/environment";


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

  private BASE_URL: string = environment.API_URL+ "/exams";

  constructor(private http: HttpClient) { }

  postNewTest(newExam: NewExam) {
    return this.http.post<string>(this.BASE_URL, newExam, httpOptions);
  }

  getAll() {
    return this.http.get<Exam[]>(this.BASE_URL+"/all");
  }

  getLatestById(id: string){
    return this.http.get<Exam>(this.BASE_URL+"/latestExam/examiner/"+id);
  }

  getExamById(id: string, examinerId: string){
    return this.http.get<Exam>(this.BASE_URL+"/exam/"+id + "/examiner/"+examinerId);
  }

  getExamsByExaminer(id: number){
    return this.http.get<Exam[]>(this.BASE_URL+"/examiner/"+id);
  }

  deleteById(examId: number) {
    console.log(examId)
    return this.http.delete(this.BASE_URL+"/delete/"+examId, httpOptions);
  }

  updateExam(updatedExam: UpdateExam){
    return this.http.put<string>(this.BASE_URL+"/update/" + updatedExam.id, updatedExam, httpOptions);
  }

}
