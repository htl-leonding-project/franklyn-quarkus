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
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatListModule} from '@angular/material/list';
import { MatSliderModule } from '@angular/material/slider';
import { MatToolbarModule } from '@angular/material/toolbar';
import {ScrollingModule} from '@angular/cdk/scrolling';
import { MdbCarouselModule } from 'mdb-angular-ui-kit/carousel';
import { MdbAccordionModule } from 'mdb-angular-ui-kit/accordion';
import { MdbCheckboxModule } from 'mdb-angular-ui-kit/checkbox';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { MdbDropdownModule } from 'mdb-angular-ui-kit/dropdown';
import { MdbFormsModule } from 'mdb-angular-ui-kit/forms';
import { MdbModalModule } from 'mdb-angular-ui-kit/modal';
import { MdbPopoverModule } from 'mdb-angular-ui-kit/popover';
import { MdbRadioModule } from 'mdb-angular-ui-kit/radio';
import { MdbRangeModule } from 'mdb-angular-ui-kit/range';
import { MdbRippleModule } from 'mdb-angular-ui-kit/ripple';
import { MdbScrollspyModule } from 'mdb-angular-ui-kit/scrollspy';
import { MdbTabsModule } from 'mdb-angular-ui-kit/tabs';
import { MdbTooltipModule } from 'mdb-angular-ui-kit/tooltip';
import { MdbValidationModule } from 'mdb-angular-ui-kit/validation';
import { NavbarComponent } from './components/navbar/navbar.component';
import { EditTestComponent } from './components/edit-test/edit-test.component';
import {MatTableModule} from '@angular/material/table';
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { NgImageFullscreenViewModule } from 'ng-image-fullscreen-view';   //for full screen

@NgModule({
  declarations: [
    AppComponent,
    StartComponent,
    DashboardComponent,
    MyTestsComponent,
    NewTestComponent,
    SidebarComponent,
    HowtoComponent,
    ScreenshotsComponent,
    NavbarComponent,
    EditTestComponent
    ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    BrowserAnimationsModule,
    MatSliderModule,
    MatListModule,
    MatToolbarModule,
    ScrollingModule,
    MdbCarouselModule,
    MdbAccordionModule,
    MdbCheckboxModule,
    MdbCollapseModule,
    MdbDropdownModule,
    MdbFormsModule,
    MdbModalModule,
    MdbPopoverModule,
    MdbRadioModule,
    MdbRangeModule,
    MdbRippleModule,
    MdbScrollspyModule,
    MdbTabsModule,
    MdbTooltipModule,
    MdbValidationModule,
    MatSelectModule,
    MatTableModule,
    MatCheckboxModule,
    NgImageFullscreenViewModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
