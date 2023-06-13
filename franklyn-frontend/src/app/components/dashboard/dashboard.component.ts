import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Exam} from 'src/app/models/exam.model';
import {ExamService} from 'src/app/services/exam.service';
import {GlobalService} from 'src/app/services/global.service';
import {LocalService} from 'src/app/services/local.service';
import {interval, Subscription, takeWhile} from 'rxjs';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {

  constructor(private router: Router, public globalService: GlobalService, private localService: LocalService, private examService: ExamService) {
  }

  examinerId: string | null = "";
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
    isToday: false,
    canBeEdited: false,
    canBeDeleted: false
  };
  subscription: Subscription = new Subscription;
  isSubscriped: boolean = false;
  currentYear: number = new Date().getFullYear();


  ngOnInit(): void {
    this.examinerId = this.localService.getData("examinerId")
    console.log(this.examinerId);
    if (this.exam.id != Number(this.localService.getData("selectedExamId"))) {
      this.getExamById();
    } else {
      console.log(this.examinerId)
      this.loadLatestExamOfExaminer(this.examinerId!);
    }
  }

  getExamById() {
    this.examService.getExamById(this.localService.getData("selectedExamId")!, this.localService.getData("examinerId")!).subscribe({
      next: data => {
        this.exam = data;
        this.localService.saveData("selectedExamId", this.exam.id + "");
        if (this.exam.isToday == true) {
          this.isSubscriped = true;
        }
        this.subscription = interval(5000)
          .pipe(
            takeWhile(() => this.isSubscriped)
          )
          .subscribe((x) => {
            this.examService.getExamById(this.localService.getData("selectedExamId")!, this.localService.getData("examinerId")!).subscribe({
              next: data => {
                this.exam = data;
                this.localService.saveData("selectedExamId", this.exam.id + "");

              },
              error: (error) => {
                alert("Fehler beim Laden des Exams: " + error.message);
              }
            });
          });
      },
      error: (error) => {
        alert("Fehler beim Laden des Exams: " + error.message);
      }
    });
  }

  loadLatestExamOfExaminer(examinerId: string) {
    this.examService.getLatestById(examinerId).subscribe({
      next: data => {
        this.exam = data
        this.localService.saveData("selectedExamId", this.exam.id + "");
        if (this.exam.isToday == true) {
          this.isSubscriped = true;
        }
        this.subscription = interval(5000)
          .pipe(
            takeWhile(() => this.isSubscriped)
          )
          .subscribe((x) => {
            this.examService.getExamById(this.localService.getData("selectedExamId")!, this.localService.getData("examinerId")!).subscribe({
              next: data => {
                this.exam = data;
                this.localService.saveData("selectedExamId", this.exam.id + "");

              },
              error: (error) => {
                alert("Fehler beim Laden des Exams: " + error.message);
              }
            });
          });
      },
      error: (error) => {
        alert("Fehler beim Laden des Exams: " + error.message);
      }
    });
  }

  logout() {
    this.localService.removeData("selectedExamId");
    this.router.navigate(['/start']);
  }

  ngOnDestroy(): void {
    this.isSubscriped = false;
  }
}
