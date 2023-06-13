import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {EditTestComponent} from './components/edit-test/edit-test.component';
import {HowtoComponent} from './components/howto/howto.component';
import {MyTestsComponent} from './components/my-tests/my-tests.component';
import {NewTestComponent} from './components/new-test/new-test.component';
import {ScreenshotsComponent} from './components/screenshots/screenshots.component';
import {StartComponent} from './components/start/start.component';
import {TestNewArchComponent} from "./test-new-arch/test-new-arch.component";

const routes: Routes = [
  {path: 'start', component: StartComponent},
  {path: 'dashboard', component: DashboardComponent},
  {path: 'newTest', component: NewTestComponent},
  {path: 'myTests', component: MyTestsComponent},
  {path: 'howto', component: HowtoComponent},
  {path: 'screenshots', component: ScreenshotsComponent},
  {path: 'editTest/:testId', component: EditTestComponent},
  {path: '', redirectTo: '/start', pathMatch: "full"},
  {path: "implementation-test", component: TestNewArchComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
