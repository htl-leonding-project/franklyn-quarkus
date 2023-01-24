import { Injectable, OnDestroy } from '@angular/core';
import { Observable, timer, Subscription, Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { switchMap, tap, share, retry, takeUntil } from 'rxjs/operators';
import { Exam } from '../models/exam.model';

@Injectable({
  providedIn: 'root'
})

@Injectable()
export class PollingService {

  private allTests!: Observable<Exam>;
  private stopPolling = new Subject();



  constructor(private http: HttpClient) {
  }

  getTestToday(id: number): Observable<Exam> {
    this.allTests = timer(1, 3000).pipe(
      switchMap(() => this.http.get<Exam>("http://localhost:8080/api/exams/exam/"+id)),
      retry(),
      tap(console.log),
      share(),
      takeUntil(this.stopPolling)
    );
    return this.allTests.pipe(   
      tap(() => console.log('data sent to subscriber'))
    );
  }
}
