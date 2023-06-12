import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class VideoService {
  private BASE_URL: string = environment.API_URL+ "/download";
  constructor(private http: HttpClient) { }

  downloadVideo(examId: string, examineeId: string){
    return this.http.get<string>(this.BASE_URL + "/video/" + examId + "/" + examineeId)
      .pipe(
        tap(console.log)
      )
  }
}
