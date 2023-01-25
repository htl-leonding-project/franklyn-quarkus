import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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
export class ScreenshotService {

  private BASE_URL: string = environment.API_URL+ "/screenshot";


  constructor(private http: HttpClient) { }

  getAllScreenshotsOfExaminee(id: string, examineeId: string) {
    return this.http.get<Screenshot[]>(this.BASE_URL+"/exam/" +id+"/examinee/"+examineeId);
  }
}
