import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Forms } from '../models/forms.model';
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class FormsService {

  private BASE_URL: string = environment.API_URL+ "schoolclass";


  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Forms[]>(this.BASE_URL);
  }

  getFormByExamId(examinerId: string){
    return this.http.get<Forms[]>(this.BASE_URL+"/exam/id/" + examinerId);
  }
}
