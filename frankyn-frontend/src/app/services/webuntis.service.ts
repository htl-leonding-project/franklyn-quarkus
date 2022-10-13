import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WebuntisService {

  constructor(private http: HttpClient) { }

  login(userName: string, passWord: string) {
    return this.http.get("http://localhost:8080/api/webuntis/authUser/" +userName+ "/"+passWord,{responseType: 'text'});
  }
}
