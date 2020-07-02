import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {RegisterComponent} from "./components/register/register.component";
import {AboutusComponent} from "./components/aboutus/aboutus.component";
import {GetstartedComponent} from "./components/getstarted/getstarted.component";
import {MainPageComponent} from "./components/main-page/main-page.component";
import {AccountComponent} from "./components/account/account.component";
import {UnauthorizedAccessComponent} from "./components/unauthorized-access/unauthorized-access.component";
import {ForgotPasswordComponent} from "./components/forgot-password/forgot-password.component";

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: '', component: HomeComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'about', component: AboutusComponent},
  {path: 'getstarted', component: GetstartedComponent},
  {path: 'mainpage', component: MainPageComponent},
  {path: 'account', component: AccountComponent},
  {path: 'unauthorizedaccess', component: UnauthorizedAccessComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
