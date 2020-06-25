import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  name:string;
  surname:string;
  email:string;
  phone:string;

  constructor(private router : Router) { }

  ngOnInit(): void {
    this.name="Bob";
    this.surname="Ross";
    this.email="bob.ross@happy_accidents.com";
    this.phone="07ceva0123";
  }

  logOut() {
    localStorage.setItem("user",null);
    this.router.navigate(["/home"]);
  }

  changePassword() {

  }
}
