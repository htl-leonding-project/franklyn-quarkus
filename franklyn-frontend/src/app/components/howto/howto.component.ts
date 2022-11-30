import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LocalService } from 'src/app/services/local.service';

@Component({
  selector: 'app-howto',
  templateUrl: './howto.component.html',
  styleUrls: ['./howto.component.css']
})
export class HowtoComponent implements OnInit {

  constructor(private router: Router, private localService: LocalService) { }

  ngOnInit(): void {
  }

  logout(){

    this.localService.clearData();
    this.router.navigate(['/start']);
  }

}
