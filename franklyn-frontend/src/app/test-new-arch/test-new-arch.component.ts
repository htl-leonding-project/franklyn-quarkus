import {Component, OnInit} from '@angular/core';
import {TestNewArchService} from "../test-new-arch.service";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {interval, map, Subscription, tap} from "rxjs";

@Component({
  selector: 'app-test-new-arch',
  templateUrl: './test-new-arch.component.html',
  styleUrls: ['./test-new-arch.component.css']
})
export class TestNewArchComponent implements OnInit {
  imageUrl: SafeUrl | null = null;
  private subscription: Subscription | null = null;

  constructor(private service: TestNewArchService, private sanitizer: DomSanitizer) {

  }

  ngOnInit(): void {
    this.getFile();
    this.subscription = interval(1000).subscribe(() => this.getFile())
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  getFile(): void {
    this.service.getLatestImage().subscribe(data => {
      const blob = new Blob([data], {type: "octet/stream"})

      this.imageUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob));
      console.log(this.imageUrl)

    })
  }

}
