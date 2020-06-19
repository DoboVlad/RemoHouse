import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SignupLoginComponent} from "./components/signup-login/signup-login.component";
import {AboutusComponent} from "./components/aboutus/aboutus.component";
import {GetstartedComponent} from "./components/getstarted/getstarted.component";
import {HomeComponent} from "./components/home/home.component";


const routes: Routes = [
  // { path: '', redirectTo: '/home', pathMatch: 'full' },
  {path: 'register', component: SignupLoginComponent},
  {path: 'about', component: AboutusComponent},
  {path: 'getstarted', component: GetstartedComponent},
  {path: 'home', component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
