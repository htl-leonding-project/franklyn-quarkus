import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Exam } from 'src/app/models/exam.model';
import { ExamService } from 'src/app/services/exam.service';
import { GlobalService } from 'src/app/services/global.service';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { LocalService } from 'src/app/services/local.service';
import { interval, Observable, Subscription, takeWhile } from 'rxjs';
import { PollingService } from 'src/app/services/polling.service';




@Component({
  selector: 'app-my-tests',
  templateUrl: './my-tests.component.html',
  styleUrls: ['./my-tests.component.css']
})
export class MyTestsComponent implements OnInit, OnDestroy {
  exams: Exam[] = [];
  closeResult = '';
  hasAlreadyExams: boolean = true;
  examinerId: string|null = "";
  examiner: string[] = [];
  forms: string[] = [];
  selectedExam: number=0
  subscription: Subscription = new Subscription;
  isSubscriped: boolean = false;

  examToday!: Observable<Exam>;

  constructor(private router: Router, private examService:ExamService, public globalService: GlobalService, private modalService: NgbModal, private localService: LocalService, private pollingService: PollingService) { 
  }
  ngOnDestroy(): void {
    this.isSubscriped = false;
  }

  ngOnInit(): void {
    this.examinerId = this.localService.getData("examinerId");
    this.loadExams();
    this.checkExamsIfToday();
    console.log("test")
    this.selectedExam = Number.parseInt(this.localService.getData("selectedExamId")!);
  }

  checkExamsIfToday(){
    console.log(this.exams.length)
    for(let exam of this.exams){
      console.log(new Date(exam.date) );
      console.log(new Date());
      if(new Date(exam.date) == new Date()){
        console.log("in if");
        this.examToday = this.pollingService.getTestToday(exam.id);
      }
    }
  }

  setExamId(examId: number) {
    this.localService.removeData("selectedExamId");
    this.localService.saveData("selectedExamId", examId +"");
    this.selectedExam = examId;
  }

  loadExams(){
    console.log(this.localService.getData("examinerId"));
    this.examService.getExamsByExaminer(Number(this.localService.getData("examinerId"))).subscribe({
      next: data => {
        this.exams = data
        console.log(this.exams.length)
        if(this.exams.length == 0){
          this.hasAlreadyExams = false;
        }
        for(let exam of this.exams){
          if(exam.isToday == true){
            this.isSubscriped = true;
          }
          this.subscription = interval(5000)
              .pipe(
            takeWhile(() => this.isSubscriped)
          )
          .subscribe((x) => {
            this.examService.getExamsByExaminer(Number(this.localService.getData("examinerId"))).subscribe({
              next: data => {
                this.exams = data;                
              }, 
              error: (error) => {alert("Fehler beim Laden der Examen: "+error.message);}
            });
          });
        }
      }, 
      error: (error) => {alert("Fehler beim Laden der Examiner: "+error.message);}
    });
  }

  deleteExam(examId: number){
    console.log(Number(this.localService.getData("selectedExamId")));
    this.examService.deleteById(Number(this.localService.getData("selectedExamId"))).subscribe({
      next: data => {
        let currentUrl = this.router.url;
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          this.router.navigate([currentUrl]);
        });
      }, 
      error: (error) => {alert("Fehler beim Löschen des Exams: "+error.message);}
    });
  }

  open(content:any) {
		this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
			(result) => {
        this.deleteExam(this.globalService.getExamId);
        console.log("test");
			},
			(reason) => {
			},
		);
	}

	private getDismissReason(reason: any): string {
		if (reason === ModalDismissReasons.ESC) {
			return 'by pressing ESC';
		} else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
			return 'by clicking on a backdrop';
		} else {
			return `with: ${reason}`;
		}
	}

  editExam(examId: number) {
    this.router.navigate(['/editTest/'+this.localService.getData('selectedExamId')])
  }

  logout(){

    this.router.navigate(['/start']);
  }
}
