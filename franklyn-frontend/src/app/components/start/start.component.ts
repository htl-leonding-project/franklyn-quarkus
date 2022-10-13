import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { WebuntisService } from 'src/app/services/webuntis.service';

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit {
  constructor(private router: Router, private webUntisService: WebuntisService) { }

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
      this.router.navigate(['/dashboard'])
    }
  }



}
