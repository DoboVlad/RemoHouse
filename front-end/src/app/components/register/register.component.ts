import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, NgForm, ValidatorFn, Validators} from "@angular/forms";
import {User} from "../../model/user";
import {UserService} from "../../service/userService";
import {Router} from "@angular/router";
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

export class RegisterComponent implements OnInit {
  private user : User;
  //validators
  nameControl= new FormControl("",
    [Validators.required, Validators.pattern(/^[A-Za-z][A-Za-z'\-]+/)]);
  phoneNoControl = new FormControl('',
    [Validators.required, Validators.minLength(10), Validators.maxLength(10),
      Validators.pattern(/^(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?([0-9]{3}(\s|\.|\-|)){2}$/)]);
  emailControl = new FormControl('',
    [Validators.required, Validators.minLength(5),
      Validators.pattern(/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/i)]);
  passwordControl = new FormControl('',
    [Validators.required, Validators.minLength(7)]);
  errorLogIn: boolean;

  constructor(private formBuilder: FormBuilder, private router: Router, private userService : UserService) {
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
    this.nameControl.markAsPristine();
    this.passwordControl.markAsPristine();
    this.emailControl.markAsPristine();
    this.phoneNoControl.markAsPristine();
    this.errorLogIn = false;
  }

  /*
  ******************IMPORTANT**********************
  localStorage.getItem("user") can be the email, or the phone number of the user
  *************************************************
   */

  signUp(name: string, surname: string, phoneNo: string, email: string, password: string) {
    this.nameControl.markAsTouched();
    this.passwordControl.markAsTouched();
    this.emailControl.markAsTouched();
    this.phoneNoControl.markAsTouched();
    if (this.nameControl.valid && this.phoneNoControl.valid && this.emailControl.valid && this.passwordControl.valid) {
      this.user = new User(0, name, surname, phoneNo, password, email);
      this.userService.signup(this.user).subscribe(response => {
        localStorage.setItem("user",this.user.phoneNumber);
        this.router.navigate(["/mainpage"]);
      }, error => {
        console.log("validation error", error);
        alert("Please try again with valid details.")
      });
    }

  }

  signIn(credential: string, password: string) {
    if(credential.indexOf("@")!=-1)
      this.user = new User(0,"","","",password,credential);
    else
      this.user = new User(0,"","",credential,password,"");
    this.userService.login(this.user).subscribe(response=>{
      if(response) {
        if(credential.indexOf("@")!=-1)
          credential = credential.split(".")[0];
        console.log(credential);
        localStorage.setItem('user',credential);
        this.router.navigate(["/mainpage"]);
      }
      else
        this.errorLogIn = true;
    },error => {
      console.log("validation error", error);
    });

  }

  getNameErrorMessage() {
    if (this.nameControl.hasError('required'))
      return 'You must enter a value';
    return 'Not a valid name';

  }

  getPhoneNoErrorMessage() {
    console.log(this.phoneNoControl.errors);
    if (this.phoneNoControl.hasError('required'))
      return 'You must enter a value';
    return 'Invalid phone number';
  }

  getEmailErrorMessage() {
    if (this.emailControl.hasError('required'))
      return 'You must enter a value';
    return 'Not a valid email';
  }

  getPasswordErrorMessage() {
    if (this.passwordControl.hasError('required'))
      return 'You must enter a value';
    return 'Not a valid password';
  }
}
