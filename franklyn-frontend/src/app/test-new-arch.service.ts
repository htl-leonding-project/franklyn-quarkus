import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {environment} from "../environments/environment";
import {retry} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class TestNewArchService {

  constructor(private http: HttpClient) {
  }

  getLatestImage(): Observable<ArrayBuffer> {
    return this.http.get<ArrayBuffer>(environment.API_URL + "/getActualImage", {
      // @ts-ignore
      responseType: "arraybuffer"
    })


  }


}
