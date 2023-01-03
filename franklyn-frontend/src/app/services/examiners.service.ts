import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Examiner } from '../models/examiner.model';

const httpOptions = {
  headers: new HttpHeaders({
  'Content-Type': 'application/json'
  //,'Authorization': 'my-auth-token'
  })
  }

@Injectable({
  providedIn: 'root'
})
export class ExaminersService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Examiner[]>("http://localhost:8080/api/examiners");
  }

  getTeacher(userName: string){
    return this.http.get<Examiner>("http://localhost:8080/api/examiners/" + userName);
  }

  getById(examinerId: string) {
    return this.http.get<Examiner>("http://localhost:8080/api/examiners/id/" + examinerId);
  }

  getExaminersByExamId(examinerId: string){
    return this.http.get<Examiner[]>("http://localhost:8080/api/examiners/exam/id/" + examinerId);
  }
}
