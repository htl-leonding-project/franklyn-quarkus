import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {
  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  newTest() {
    this.router.navigate(['/newTest'])
  }

  ShowMyTests() {
    this.router.navigate(['/myTests'])
  }

  ShowDashboard() {
    this.router.navigate(['/dashboard'])
  }
  ShowExplanation() {
    this.router.navigate(['/howto'])
  }

  ShowScreenshots() {
    this.router.navigate(['/screenshots'])
  }


}
