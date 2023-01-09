import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

const httpOptions = {
  headers: new HttpHeaders({
  'Content-Type': 'application/json',
  //,'Authorization': 'my-auth-token'
  'responseType': 'text',
  })
  }
  
@Injectable({
  providedIn: 'root'
})
export class WebuntisService {

  private BASE_URL: string = "http://localhost:8080/api/webuntis";

  constructor(private http: HttpClient) { }

  login(userName: string, passWord: string) {
    return this.http.post(this.BASE_URL+"/authUser/" +userName,passWord, {responseType: 'text'});
  }
}
