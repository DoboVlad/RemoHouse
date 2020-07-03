import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HomeComponent} from "./components/home/home.component";
import {RegisterComponent} from "./components/register/register.component";
import {AboutusComponent} from "./components/aboutus/aboutus.component";
import {GetstartedComponent} from "./components/getstarted/getstarted.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UserService} from "./service/userService";
import {RoomService} from "./service/roomService";
import {LocationService} from "./service/locationService";
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RouterModule} from "@angular/router";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MainPageComponent} from "./components/main-page/main-page.component";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatListModule} from "@angular/material/list";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {AccountComponent} from './components/account/account.component';
import { UnauthorizedAccessComponent } from './components/unauthorized-access/unauthorized-access.component';
import {GsmControllerService} from "./service/gsmControllerService";
import {MatDialogModule,MAT_DIALOG_DEFAULT_OPTIONS} from "@angular/material/dialog";
import {MatButtonModule} from "@angular/material/button";
import { ChangePasswordDialogComponent } from './components/change-password-dialog/change-password-dialog.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RegisterComponent,
    AboutusComponent,
    GetstartedComponent,
    MainPageComponent,
    AccountComponent,
    UnauthorizedAccessComponent,
    ChangePasswordDialogComponent,
    ForgotPasswordComponent
  ],
    imports: [
        BrowserModule,
        RouterModule,
        HttpClientModule,
        AppRoutingModule,
        FormsModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatSidenavModule,
        MatGridListModule,
        MatListModule,
        MatSlideToggleModule,
        MatSnackBarModule,
      MatDialogModule,
      MatButtonModule
    ],
  entryComponents:[ChangePasswordDialogComponent],
  providers: [UserService, RoomService, LocationService, GsmControllerService],
  bootstrap: [AppComponent,UnauthorizedAccessComponent]
})
export class AppModule {}
