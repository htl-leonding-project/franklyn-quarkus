import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Exam } from 'src/app/models/exam.model';
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
  exam: Exam = {
    pin: '',
    title: '',
    ongoing: '',
    date: '',
    startTime: new Date(),
    forms: '',
    nrOfStudents: '',
    examiners: '',
    id: 0
  };
  
  ngOnInit(): void {
    this.examinerId = this.localService.getData("examinerId")
    console.log(this.examinerId);
    if(this.exam.id != Number(this.localService.getData("selectedExamId"))){
      this.getExamById();
    }
    else{
      console.log(this.examinerId)
      this.loadLatestExamOfExaminer(this.examinerId!);
    }
  }

  getExamById() {
    this.examService.getById(this.localService.getData("selectedExamId")!).subscribe({
      next: data => {
        this.exam = data;
        this.localService.saveData("selectedExamId", this.exam.id+"");
      }, 
      error: (error) => {alert("Fehler beim Laden des Exams: "+error.message);}
    });
  }
  
  loadLatestExamOfExaminer(examinerId: string) {
    this.examService.getLatestById(examinerId).subscribe({
      next: data => {
        this.exam = data
        this.localService.saveData("selectedExamId", this.exam.id+"");
      }, 
      error: (error) => {alert("Fehler beim Laden des Exams: "+error.message);}
    });
  }

  logout(){

    this.localService.clearData();
    this.router.navigate(['/start']);
  }
}
