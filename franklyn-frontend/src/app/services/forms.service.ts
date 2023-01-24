import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Forms } from '../models/forms.model';

@Injectable({
  providedIn: 'root'
})
export class FormsService {

  private BASE_URL: string = "http://localhost:8080/api/schoolclass";


  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Forms[]>(this.BASE_URL);
  }

  getFormByExamId(examinerId: string){
    return this.http.get<Forms[]>(this.BASE_URL+"/exam/id/" + examinerId);
  }
}
