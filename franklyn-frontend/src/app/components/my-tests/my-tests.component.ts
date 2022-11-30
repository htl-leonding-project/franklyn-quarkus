import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Exam } from 'src/app/models/exam.model';
import { ExamService } from 'src/app/services/exam.service';
import { GlobalService } from 'src/app/services/global.service';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { LocalService } from 'src/app/services/local.service';
import { Observable } from 'rxjs';
import { PollingService } from 'src/app/services/polling.service';




@Component({
  selector: 'app-my-tests',
  templateUrl: './my-tests.component.html',
  styleUrls: ['./my-tests.component.css']
})
export class MyTestsComponent implements OnInit {
  exams: Exam[] = [];
  closeResult = '';
  hasAlreadyExams: boolean = true;
  examinerId: string|null = "";
  examiner: string[] = [];
  forms: string[] = [];
  selectedExam: number=0

  examToday!: Observable<Exam>;

  constructor(private router: Router, private examService:ExamService, public globalService: GlobalService, private modalService: NgbModal, private localService: LocalService, private pollingService: PollingService) { 
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
    this.examService.getExamsByExaminer(Number(this.examinerId)).subscribe({
      next: data => {
        this.exams = data
        console.log(this.exams.length)
        if(this.exams.length == 0){
          this.hasAlreadyExams = false;
        }
      }, 
      error: (error) => {alert("Fehler beim Laden der Examiner: "+error.message);}
    });
  }

  deleteExam(examId: number){
    this.examService.deleteById(examId).subscribe({
      next: data => {
        this.router.navigate(['/myTests'])
      }, 
      error: (error) => {alert("Fehler beim Löschen des Exams: "+error.message);}
    });
  }

  open(content:any) {
		this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
			(result) => {
        this.deleteExam(this.globalService.getExamId);
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

    this.localService.clearData();
    this.router.navigate(['/start']);
  }
}
