import { ThisReceiver } from '@angular/compiler';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { interval, Subscription, takeWhile } from 'rxjs';
import { Exam } from 'src/app/models/exam.model';
import { Examinee } from 'src/app/models/examinee.model';
import { Screenshot } from 'src/app/models/screenshot.model';
import { ExamService } from 'src/app/services/exam.service';
import { ExamineeService } from 'src/app/services/examinee.service';
import { GlobalService } from 'src/app/services/global.service';
import { LocalService } from 'src/app/services/local.service';
import { ScreenshotService } from 'src/app/services/screenshot.service';


@Component({
  selector: 'app-screenshots',
  templateUrl: './screenshots.component.html',
  styleUrls: ['./screenshots.component.css'],
})
export class ScreenshotsComponent implements OnInit, OnDestroy {

  constructor(private examineeService: ExamineeService, public globalService: GlobalService, private localService: LocalService, private examService: ExamService, private screenshotService: ScreenshotService, private router: Router) { }

  currentScreenshot: Screenshot = {
    pathOfScreenshot: '',
    screenshotId: 0,
    examineeId: 0,
    examId: 0
  }

  currentIdx: number = 0;

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
    pathOfScreenshot: "../../../assets/img/temp3.png",
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
    forms: '',
    nrOfStudents: '',
    examiners: '',
    id: 0,
    isToday: false
  };
  examinees: Examinee[] = [];
  screenshotsOfExaminee: Screenshot[]=[];
  selectedExamineeId: string = "";
  currentSelectedExamIsToday: boolean = true;
  subscription: Subscription = new Subscription;
  isSubscriped: boolean = true;


  ngOnInit(): void {
    this.loadStudents();
    this.loadExam();
    this.currentScreenshot = this.screenshots[0];

  }

  loadStudents() {
    this.examineeService.getExamineesByExamId(Number(this.localService.getData("selectedExamId"))).subscribe({
      next: data => {
        this.examinees = data;
        if(this.examinees.length > 0){
          this.selectedExamineeId = this.examinees[0].id;
        }
      }, 
      error: (error) => {alert("Fehler beim Laden der Examinees: "+error.message);}
    });
  }

  SelectExaminee(examineeId: string) {
    this.isSubscriped = false
    this.screenshotService.getAllScreenshotsOfExaminee(this.localService.getData("selectedExamId")!, examineeId).subscribe({
      next: data => {
        this.screenshots = data;
      }, 
      error: (error) => {alert("Fehler beim Laden der Screenshots: "+error.message);}
    });
    this.selectedExamineeId = examineeId;
    if(this.currentSelectedExamIsToday){
      this.subscription = interval(5000)
      .pipe(
        takeWhile(() => this.isSubscriped)
      )
      .subscribe((x) => {
        this.screenshotService.getAllScreenshotsOfExaminee(this.localService.getData("selectedExamId")!, this.selectedExamineeId).subscribe({
          next: data => {
            this.screenshots = data;
          }, 
          error: (error) => {alert("Fehler beim Laden der Screenshots: "+error.message);}
        });
      });
      this.isSubscriped = true;
      this.subscription = interval(5000)
      .pipe(
        takeWhile(() => this.isSubscriped)
      )
      .subscribe((x) => {
        this.screenshotService.getAllScreenshotsOfExaminee(this.localService.getData("selectedExamId")!, this.selectedExamineeId).subscribe({
          next: data => {
            this.screenshots = data;
          }, 
          error: (error) => {alert("Fehler beim Laden der Screenshots: "+error.message);}
        });
      });
    }
/*     this.screenshots = this.screenshots.reverse();
    this.currentScreenshot = this.screenshots[0]; */

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
    if((this.screenshots.length -1) >= (this.currentIdx +1)){
      this.currentIdx = this.currentIdx + 1;
    }
    this.currentScreenshot = this.screenshots[this.currentIdx];
  }
  goToPrevious() {
    if(0 <= (this.currentIdx -1)){
      this.currentIdx = this.currentIdx - 1;
    }
    this.currentScreenshot = this.screenshots[this.currentIdx];
  }

  logout(){

    this.router.navigate(['/start']);
  }

  ngOnDestroy(): void {
    this.isSubscriped = false;
  }
}
