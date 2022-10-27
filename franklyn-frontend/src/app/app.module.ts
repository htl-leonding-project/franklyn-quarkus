import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { StartComponent } from './components/start/start.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { MyTestsComponent } from './components/my-tests/my-tests.component';
import { NewTestComponent } from './components/new-test/new-test.component';
import { FormsModule } from '@angular/forms';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HttpClientModule } from '@angular/common/http';
import { HowtoComponent } from './components/howto/howto.component';
import { ScreenshotsComponent } from './components/screenshots/screenshots.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';



@NgModule({
  declarations: [
    AppComponent,
    StartComponent,
    DashboardComponent,
    MyTestsComponent,
    NewTestComponent,
    SidebarComponent,
    HowtoComponent,
    ScreenshotsComponent
    ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
