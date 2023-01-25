import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Examinee } from '../models/examinee.model';
import { Screenshot } from '../models/screenshot.model';
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
export class ExamineeService {
  private BASE_URL: string = environment.API_URL;


  constructor(private http: HttpClient) { }

  getExamineesByExamId(id: number){
    return this.http.get<Examinee[]>(this.BASE_URL+"/examinees/exam/"+id);
  }

  getLatestScreenshotByExamIdAndExamineeId(examId: number, examineeId: number){
    return this.http.get<Screenshot>(this.BASE_URL+"/screenshot/exam/"+examId+"/examinee/"+examineeId);
  }
}
