import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {


  private _examId: number = 0;
  public get getExamId(): number {
    return this._examId;
  }
  public set setExamId(value: number) {
    this._examId = value;
  }

  constructor() { }
}
