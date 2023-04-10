import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  private BASE_URL: string = environment.API_URL+ "/download";
  constructor(private http: HttpClient) { }

  downloadVideoByExamineeIdAndExamId(examineeId: string, examId: string){
    return this.http.get<string>(this.BASE_URL + "/video/" + examId + "/" + examineeId);
  }
}
