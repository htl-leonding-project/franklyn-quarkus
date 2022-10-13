import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { HowtoComponent } from './components/howto/howto.component';
import { MyTestsComponent } from './components/my-tests/my-tests.component';
import { NewTestComponent } from './components/new-test/new-test.component';
import { ScreenshotsComponent } from './components/screenshots/screenshots.component';
import { StartComponent } from './components/start/start.component';

const routes: Routes = [
  {path: 'start', component: StartComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'newTest', component: NewTestComponent},
  {path: 'myTests', component: MyTestsComponent},
  {path: 'howto', component: HowtoComponent},
  {path: 'screenshots', component: ScreenshotsComponent},
  {path: '', redirectTo: '/start', pathMatch:"full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
