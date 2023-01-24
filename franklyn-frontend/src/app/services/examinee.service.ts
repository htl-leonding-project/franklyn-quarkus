import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Examinee } from '../models/examinee.model';
import { Screenshot } from '../models/screenshot.model';

const httpOptions = {
  headers: new HttpHeaders({
  'Content-Type': 'application/json'
  //,'Authorization': 'my-auth-token'
  })
  }


@Injectable({
  providedIn: 'root'
})
export class ExamineeService {

  constructor(private http: HttpClient) { }

  getExamineesByExamId(id: number){
    return this.http.get<Examinee[]>("http://localhost:8080/api/examinees/exam/"+id);
  }

  getLatestScreenshotByExamIdAndExamineeId(examId: number, examineeId: number){
    return this.http.get<Screenshot>("http://localhost:8080/api/screenshot/exam/"+examId+"/examinee/"+examineeId);
  }
}
