import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Exam } from 'src/app/models/exam.model';
import { ExamService } from 'src/app/services/exam.service';
import { GlobalService } from 'src/app/services/global.service';

@Component({
  selector: 'app-my-tests',
  templateUrl: './my-tests.component.html',
  styleUrls: ['./my-tests.component.css']
})
export class MyTestsComponent implements OnInit {

  exams: Exam[] =[]

  constructor(private router: Router, private examService:ExamService, public globalService: GlobalService) { }

  ngOnInit(): void {
    this.loadExams();
  }
  loadExams() {
    this.examService.getAll().subscribe({
      next: data => {
        this.exams = data
      }, 
      error: (error) => {alert("Fehler beim Laden der Examiner: "+error.message);}
    });
  }

  

}
