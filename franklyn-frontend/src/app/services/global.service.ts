import { Injectable } from '@angular/core';
import { Examiner } from '../models/examiner.model';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {


  private _loggedInExaminer: Examiner = {lastName: "", firstName: "", id: 0};


  private _examId: number = 0;

  public get getExamId(): number {
    return this._examId;
  }
  public set setExamId(value: number) {
    this._examId = value;
  }

  public get getExaminer(): Examiner {
    return this._loggedInExaminer;
  }
  public set setExaminer(value: Examiner) {
    this._loggedInExaminer = value;
  }


  constructor() { }
}
