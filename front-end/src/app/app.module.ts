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
import {HttpClient, HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    RegisterComponent,
    AboutusComponent,
    GetstartedComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [UserService, RoomService, LocationService],
  bootstrap: [AppComponent]
})
export class AppModule {}
