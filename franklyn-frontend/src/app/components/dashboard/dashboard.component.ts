import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GlobalService } from 'src/app/services/global.service';
import { LocalService } from 'src/app/services/local.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  constructor(private router: Router, public globalService: GlobalService, private localService: LocalService) { }

  examinerId: string|null = "";
  
  ngOnInit(): void {
    this.examinerId = this.localService.getData("examinerId")
  }

}
