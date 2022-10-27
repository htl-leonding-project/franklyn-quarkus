import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ExaminersService } from 'src/app/services/examiners.service';
import { GlobalService } from 'src/app/services/global.service';
import { WebuntisService } from 'src/app/services/webuntis.service';

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit {
  constructor(private router: Router, private webUntisService: WebuntisService, public globalService: GlobalService, private examinerService: ExaminersService) { }

  userName: string = "";
  passWord: string = "";

  response: string = "";

  ngOnInit(): void {
  }


  onSubmit(resultForm: NgForm) {

  this.webUntisService.login(this.userName,this.passWord).subscribe({
      next: data => {
        this.response = data
      }, 
      error: (error) => {alert("Fehler beim Login: "+error.message);}
    });
    if(this.response == "success"){
      this.loadExaminer();
      this.router.navigate(['/dashboard'])
    }
  }
  loadExaminer() {
    this.examinerService.getTeacher(this.userName).subscribe({
      next: data => {
        this.globalService.setExaminer = data
      }, 
      error: (error) => {alert("Fehler beim Laden des Examiner: "+error.message);}
    });
  }

}
