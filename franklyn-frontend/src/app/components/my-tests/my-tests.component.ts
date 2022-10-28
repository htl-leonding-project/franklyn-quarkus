import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Exam } from 'src/app/models/exam.model';
import { ExamService } from 'src/app/services/exam.service';
import { GlobalService } from 'src/app/services/global.service';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { LocalService } from 'src/app/services/local.service';


@Component({
  selector: 'app-my-tests',
  templateUrl: './my-tests.component.html',
  styleUrls: ['./my-tests.component.css']
})
export class MyTestsComponent implements OnInit {
  exams: Exam[] =[];
  closeResult = '';
  hasAlreadyExams: boolean = true;
  examinerId: string|null = "";

  constructor(private router: Router, private examService:ExamService, public globalService: GlobalService, private modalService: NgbModal, private localService: LocalService) { }

  ngOnInit(): void {
    this.examinerId = this.localService.getData("examinerId");
    this.loadExams();
  }
  loadExams() {
    this.examService.getExamsByExaminer(Number(this.examinerId)).subscribe({
      next: data => {
        this.exams = data
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
    throw new Error('Method not implemented.');
  }

  

}
