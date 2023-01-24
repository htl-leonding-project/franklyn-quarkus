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

  private BASE_URL: string = "http://localhost:8080/api/examiners";


  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Examiner[]>(this.BASE_URL);
  }

  getTeacher(userName: string){
    return this.http.get<Examiner>(this.BASE_URL + "/"+ userName);
  }

  getById(examinerId: string) {
    return this.http.get<Examiner>(this.BASE_URL+"/id/" + examinerId);
  }

  getExaminersByExamId(examId: string, examinerId: string){
    return this.http.get<Examiner[]>(this.BASE_URL+"/exam/" + examId + "/examiner/" + examinerId);
  }
}
