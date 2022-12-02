import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Exam } from 'src/app/models/exam.model';
import { Examiner } from 'src/app/models/examiner.model';
import { Forms } from 'src/app/models/forms.model';
import { NewExam } from 'src/app/models/new-exam.model';
import { ExamService } from 'src/app/services/exam.service';
import { LocalService } from 'src/app/services/local.service';

@Component({
  selector: 'app-edit-test',
  templateUrl: './edit-test.component.html',
  styleUrls: ['./edit-test.component.css']
})
export class EditTestComponent implements OnInit {

  constructor(private localService: LocalService, private examService: ExamService, private router:Router) { }

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
  }

  newExam: NewExam = {
    title: '',
    formIds: [],
    examinerIds: [],
    date: new Date("Fri Dec 08 2019 07:44:57").toString(),
    startTime: '',
    endTime: '',
    interval: '',
  }

  form_Id: number = 0;
  examiner_Id: number = 0;
  tempExaminerId: string = "";
  tempFormId: string = "";
  tempDate: Date = new Date("Fri Dec 08 2019 07:44:57");
  tempInterval: number = 5;
  tempStartTime: Date = new Date("Fri Dec 08 2019 07:44:57");
  tempEndTime: Date = new Date("Fri Dec 08 2019 07:44:57");

  selectedForms: number[] = [];
  selectedExaminers: number[] = [];


  examiners: Examiner[] = [];
  forms: Forms[] = [];

  examinerId: string|null = "";

  ngOnInit(): void {
    this.examinerId = this.localService.getData("examinerId");
    if(this.exam.id != Number(this.localService.getData("selectedExamId"))){
      this.getExamById();
      //this.newExam.title = this.exam.title;
      //this.newExam.date = this.exam.date;

    }
    else{
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

  save() {
    throw new Error('Method not implemented.');
  }

  logout(){

    this.router.navigate(['/start']);
  }

}


