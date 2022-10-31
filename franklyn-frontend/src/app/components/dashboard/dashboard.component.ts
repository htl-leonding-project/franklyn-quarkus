import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ExamService } from 'src/app/services/exam.service';
import { GlobalService } from 'src/app/services/global.service';
import { LocalService } from 'src/app/services/local.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private router: Router, public globalService: GlobalService, private localService: LocalService, private examService: ExamService) { }

  examinerId: string|null = "";
  
  ngOnInit(): void {
    this.examinerId = this.localService.getData("examinerId")
    //this.loadLatestExamOfExaminer(this.examinerId!);
  }
  
  loadLatestExamOfExaminer(examinerId: string) {
    this.examService.getById(examinerId).subscribe({
      next: data => {
        this.router.navigate(['/myTests'])
      }, 
      error: (error) => {alert("Fehler beim Löschen des Exams: "+error.message);}
    });
  }

}
