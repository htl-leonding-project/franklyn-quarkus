import { Component, OnInit } from '@angular/core';
import { Exam } from 'src/app/models/exam.model';
import { Examinee } from 'src/app/models/examinee.model';
import { Screenshot } from 'src/app/models/screenshot.model';
import { ExamService } from 'src/app/services/exam.service';
import { ExamineeService } from 'src/app/services/examinee.service';
import { GlobalService } from 'src/app/services/global.service';
import { LocalService } from 'src/app/services/local.service';

@Component({
  selector: 'app-screenshots',
  templateUrl: './screenshots.component.html',
  styleUrls: ['./screenshots.component.css']
})
export class ScreenshotsComponent implements OnInit {

  constructor(private examineeService: ExamineeService, public globalService: GlobalService, private localService: LocalService, private examService: ExamService) { }

  toggle = true;
  status = "Not";

  screenshots: Screenshot[] = [{
    pathOfScreenshot: "../../../assets/img/temp.png",
    screenshotId: 1,
    examineeId: 1,
    examId: 1
  },{
    pathOfScreenshot: "../../../assets/img/temp2.png",
    screenshotId: 1,
    examineeId: 1,
    examId: 1
  },{
    pathOfScreenshot: "../../../assets/img/temp2.png",
    screenshotId: 1,
    examineeId: 1,
    examId: 1
  }];

  exam: Exam = {
    pin: '',
    title: '',
    ongoing: '',
    date: '',
    startTime: new Date(),
    formIds: '',
    nrOfStudents: '',
    examinerIds: '',
    id: 0
  };
  examinees: Examinee[] = [];

  ngOnInit(): void {
    this.loadStudents();
    this.loadExam();
  }

  loadStudents() {
    this.examineeService.getExamineesByExamId(Number(this.localService.getData("selectedExamId"))).subscribe({
      next: data => {
        this.examinees = data;
      }, 
      error: (error) => {alert("Fehler beim Laden der Examinees: "+error.message);}
    });
  }

  SelectExaminee(examineeId: string) {
    this.toggle = !this.toggle;
    this.status = this.toggle ? "Selected" : "Not";
  }

  loadExam() {
    this.examService.getExamById(this.localService.getData("selectedExamId")!).subscribe({
      next: data => {
        this.exam = data;
      }, 
      error: (error) => {alert("Fehler beim Laden des Exams: "+error.message);}
    });
  }
  goToNext() {
    throw new Error('Method not implemented.');
  }
  goToPrevious() {
    throw new Error('Method not implemented.');
  }
}
