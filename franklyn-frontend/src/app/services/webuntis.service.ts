import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";

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

  private BASE_URL: string = environment.API_URL+ "/webuntis";

  constructor(private http: HttpClient) { }

  login(userName: string, passWord: string) {
    return this.http.post(this.BASE_URL+"/authUser/" +userName,passWord, {responseType: 'text'});
  }
}
