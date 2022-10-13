import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Forms } from '../models/forms.model';

@Injectable({
  providedIn: 'root'
})
export class FormsService {

  constructor(private http: HttpClient) { }

  getAll(){
    return this.http.get<Forms[]>("http://localhost:8080/api/schoolclass");
  }
}
