import { Component, OnInit } from '@angular/core';
import { Examinee } from 'src/app/models/examinee.model';
import { ExamineeService } from 'src/app/services/examinee.service';
import { GlobalService } from 'src/app/services/global.service';

@Component({
  selector: 'app-screenshots',
  templateUrl: './screenshots.component.html',
  styleUrls: ['./screenshots.component.css']
})
export class ScreenshotsComponent implements OnInit {

  constructor(private examineeService: ExamineeService, public globalService: GlobalService) { }

  examinees: Examinee[] = [];

  ngOnInit(): void {
    this.loadStudents();
  }
  loadStudents() {
    this.examineeService.getExamineesByExamId(this.globalService.getExamId).subscribe({
      next: data => {
        this.examinees = data;
      }, 
      error: (error) => {alert("Fehler beim Laden der Examinees: "+error.message);}
    });
  }

}
