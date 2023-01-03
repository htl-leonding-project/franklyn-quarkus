import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Exam } from 'src/app/models/exam.model';
import { Examiner } from 'src/app/models/examiner.model';
import { Forms } from 'src/app/models/forms.model';
import { NewExam } from 'src/app/models/new-exam.model';
import { ExamService } from 'src/app/services/exam.service';
import { ExaminersService } from 'src/app/services/examiners.service';
import { FormsService } from 'src/app/services/forms.service';
import { LocalService } from 'src/app/services/local.service';
import { Form, FormBuilder, FormControl, FormGroup,ReactiveFormsModule } from '@angular/forms';
import { UpdateExam } from 'src/app/models/update-exam.model';

@Component({
  selector: 'app-edit-test',
  templateUrl: './edit-test.component.html',
  styleUrls: ['./edit-test.component.css']
})
export class EditTestComponent implements OnInit {

  constructor(private localService: LocalService, private examService: ExamService, private router:Router, private examinersService: ExaminersService, private formService: FormsService, private formBuilder: FormBuilder) {
   }

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
  }

  newExam: UpdateExam = {
    id: 0,
    title: '',
    formIds: [],
    examinerIds: [],
    date: new Date("Fri Dec 08 2019 07:44:57").toString(),
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

  selectedFormsObjects: Forms[] = [];
  selectedExaminersObjects: Examiner[] = [];
  selectedExaminersAny: any = [];
  selectedFormsAny: any = [];
  tempExaminers: Examiner[] = [];
  tempForms: Forms[] = [];

  examiners: Examiner[] = [];
  forms: Forms[] = [];

  examinerId: string|null = "";

  selectedExaminersControl = new FormControl();


  ngOnInit(): void {
    this.examinerId = this.localService.getData("examinerId");
    if(this.exam.id != Number(this.localService.getData("selectedExamId"))){
      this.getExamById();
      this.loadExaminers();
      this.loadForms();
      this.loadSelectedExaminer();
      this.loadSelectedForms();
    }
  }

  loadSelectedExaminer(){
    this.examinersService.getExaminersByExamId(this.localService.getData("selectedExamId")!).subscribe({
      next: data => {
        this.selectedExaminersObjects = data;
        for(let examiner of this.selectedExaminersObjects){
          this.selectedExaminersAny.push(examiner.id);
        }
      }, 
      error: (error) => {alert("Fehler beim Laden des Examiner: "+error.message);}
    });
  }

  loadSelectedForms(){
    this.formService.getFormByExamId(this.localService.getData("selectedExamId")!).subscribe({
      next: data => {
        this.selectedFormsObjects = data;
        for(let form of this.selectedFormsObjects){
          this.selectedFormsAny.push(form.id);
        }
      }, 
      error: (error) => {alert("Fehler beim Laden der Klassen: "+error.message);}
    });
  }

  getExamById() {
    this.examService.getById(this.localService.getData("selectedExamId")!).subscribe({
      next: data => {
        this.exam = data;
        this.newExam.id = this.exam.id;
      }, 
      error: (error) => {alert("Fehler beim Laden des Exams: "+error.message);}
    });
  }

  loadExaminers() {
    this.examinersService.getAll().subscribe({
      next: data => {
        this.examiners = data;
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

  save() {
    this.newExam.examinerIds.push(this.localService.getData("examinerId") + '');
    for(let s of this.selectedExaminersAny){
      this.tempExaminerId = s +'';
      this.newExam.examinerIds.push(this.tempExaminerId);
      console.log(this.tempExaminerId);
    }
    for(let s of this.selectedFormsAny){
      this.tempFormId = s +'';
      this.newExam.formIds.push(this.tempFormId);
    }
    this.newExam.date = this.tempDate.toString();
    this.newExam.interval = this.tempInterval + '';


    this.examService.updateExam(this.newExam).subscribe({
      next: data =>{
      }, 
      error: (error)=>{alert("Exam konnte nicht editiert werden!")}
    })
  }

  logout(){
    this.localService.removeData("selectedExamId");
    this.router.navigate(['/start']);
  }

  comparer(o1: Examiner, o2: Examiner): boolean {
    // if possible compare by object's name, and not by reference.
    return o1 && o2 ? o1.id === o2.id : o2 === o2;
  }

}


