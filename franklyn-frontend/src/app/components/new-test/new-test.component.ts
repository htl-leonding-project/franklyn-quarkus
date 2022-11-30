import { Time } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Form, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { Examiner } from 'src/app/models/examiner.model';
import { Forms } from 'src/app/models/forms.model';
import { NewExam } from 'src/app/models/new-exam.model';
import { ExamService } from 'src/app/services/exam.service';
import { ExaminersService } from 'src/app/services/examiners.service';
import { FormsService } from 'src/app/services/forms.service';
import { GlobalService } from 'src/app/services/global.service';
import { LocalService } from 'src/app/services/local.service';

@Component({
  selector: 'app-new-test',
  templateUrl: './new-test.component.html',
  styleUrls: ['./new-test.component.css']
})
export class NewTestComponent implements OnInit {
  showFirstCard: boolean= true;
  showSecondCard: boolean=false;
  showThirdCard: boolean=false;
  showFourthCard: boolean=false;

  startTime: number = Date.now();
  rangeValue: number = 0;
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

  constructor(private examinersService: ExaminersService, private formService: FormsService, private examService: ExamService, private router: Router, public globalService: GlobalService, private localService: LocalService) {
    setInterval(() => {this.startTime = Date.now()}, 1);
   }

  ngOnInit(): void {
    this.loadExaminers();
    this.loadForms();
  }

  firstCardSubmit() {
    this.showFirstCard=false;
    //this.showSecondCard = true;
    this.showThirdCard = true;
  }

  secondCardSubmit() {
    this.showSecondCard=false;
    this.showThirdCard = true;
  }

  thirdCardSubmit() {
    this.showSecondCard = false;
    this.showThirdCard = false;

    
    //this.tempExaminerId = this.examiner_Id + '';
    //this.tempFormId = this.form_Id + '';
    this.newExam.examinerIds.push(this.localService.getData("examinerId") + '');
    for(let s of this.selectedForms){
      this.tempExaminerId = s +'';
      this.newExam.examinerIds.push(this.tempExaminerId);
    }
    for(let s of this.selectedForms){
      this.tempFormId = s +'';
      this.newExam.formIds.push(this.tempFormId);
    }
    this.newExam.date = this.tempDate.toString();
    this.newExam.interval = this.tempInterval + '';
    this.newExam.startTime = this.tempStartTime + '';
    this.newExam.endTime = this.tempEndTime + '';

    this.examService.postNewTest(this.newExam).subscribe({
      next: data =>{
        this.showFourthCard = true;
      }, 
      error: (error)=>{alert("Konnte nicht gespeichert werden!")}
    })
  }

  secondCardBack() {
    this.showFirstCard = true;
    this.showSecondCard = false;
    this.showThirdCard = false;
  }

  thirdCardBack(){
    this.showFirstCard = true;
    //this.showSecondCard = true;
    this.showThirdCard = false;
  }

  loadExaminers() {
    this.examinersService.getAll().subscribe({
      next: data => {
        this.examiners = data
      }, 
      error: (error) => {alert("Fehler beim Laden der Examiner: "+error.message);}
    });
  }

  loadForms() {
    this.formService.getAll().subscribe({
      next: data => {
        this.forms = data
      }, 
      error: (error) => {alert("Fehler beim Laden der Schulklassen: "+error.message);}
    });
  }

  logout(){

    this.localService.clearData();
    this.router.navigate(['/start']);
  }

}
