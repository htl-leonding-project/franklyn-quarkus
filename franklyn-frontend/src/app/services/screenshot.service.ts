import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
export class ScreenshotService {

  constructor(private http: HttpClient) { }

  getAllScreenshotsOfExaminee(id: string, examineeId: string) {
    return this.http.get<Screenshot[]>("http://localhost:8080/api/screenshot/exam/" +id+"/examinee/"+examineeId);
  }
}
