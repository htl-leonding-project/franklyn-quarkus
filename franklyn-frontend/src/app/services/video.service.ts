import { Injectable } from '@angular/core';
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})

export class VideoService {
  private BASE_URL: string = environment.API_URL+ "/download";
  constructor(private http: HttpClient) { }

  downloadVideo(examineeId: string, examId: string){
    return this.http.get<string>(this.BASE_URL + "/video/" + examId + "/" + examineeId);
  }
}
