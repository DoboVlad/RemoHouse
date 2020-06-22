import { Component, OnInit } from '@angular/core';
import {FormGroup, NgForm} from "@angular/forms";
import {User} from "../../model/user";
import {UserService} from "../../service/userService";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  private user : User;

  constructor(private router: Router, private userService : UserService) {
  }

  ngOnInit(): void {
    const signUpButton = document.getElementById('signUp');
    const signInButton = document.getElementById('signIn');
    const container = document.getElementById('container');

    signUpButton.addEventListener('click', () => {
      container.classList.add("right-panel-active");
    });

    signInButton.addEventListener('click', () => {
      container.classList.remove("right-panel-active");
    });
  }

  signUp(name: string, surname: string, phoneNo: string, email: string, password: string) {
    this.user = new User(0,name,surname,phoneNo,password,email);
    this.userService.signup(this.user).subscribe(response=>{
      this.router.navigate(["/mainpage",{user:this.user}]);
    },error => console.log("validation error", error));

  }

  signIn(email: string, password: string) {
    this.user = new User(0,"","","",password,email);
    this.userService.login(this.user).subscribe(response=>{
      this.router.navigate(["/mainpage",{user:response}]);
    },error => console.log("validation error", error));

  }
}
