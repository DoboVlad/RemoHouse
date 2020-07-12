import { Component, OnInit } from '@angular/core';
import {UserService} from "../../service/userService";
import {User} from "../../model/user";
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  private user : User;
  private credential : string;

  constructor(private userService : UserService) {
  }

  ngOnInit(): void {
  }
  emailControllForm= new FormControl('',[
    Validators.required,
    Validators.minLength(5),
    Validators.pattern(/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/i)
  ]);

  phoneControllForm= new FormControl('',[
    Validators.required,
    Validators.minLength(10),
    Validators.maxLength(10),
    Validators.pattern(/^(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?([0-9]{3}(\s|\.|\-|)){2}$/)
  ]);

  codeControlForm= new FormControl('',[
    Validators.required,
    Validators.minLength(6),
    Validators.maxLength(6)
  ]);

  passwordControlForm = new FormControl('', [
      Validators.required,
      Validators.minLength(7)
  ]);


  resetPassword() {
    document.getElementById("titlu").innerHTML = "You can reset your password here!<br> Just enter your phone number!";
    document.getElementById("reset").style.display = "none";
    document.getElementById("resetPhone").style.display = "inline-block";
  }

  resetPassword1() {
    document.getElementById("titlu").innerHTML = "You can reset your password here!<br> Just enter your email!";
    document.getElementById("reset").style.display = "none";
    document.getElementById("resetEmail").style.display = "inline-block";
  }

  cancel() {
    document.getElementById("titlu").innerHTML = "How would you like to reset your password?";
    document.getElementById("reset").style.display = "inline";
    document.getElementById("resetEmail").style.display = "none";
    document.getElementById("resetPhone").style.display = "none";
  }

  reset(c:string,credential:string) {
    console.log(credential);
    this.credential=credential;
    this.userService.getUserByCredential(credential.split(".")[0]).subscribe(result=> {
        this.user=result;
        document.getElementById("titlu").innerHTML = "Please enter your code below!";
        document.getElementById("reset1").style.display = "block";
        document.getElementById("reset").style.display = "none";
        document.getElementById("resetEmail").style.display = "none";
        document.getElementById("resetPhone").style.display = "none";
        document.getElementById("span1").innerHTML = c;
        this.userService.sendCode(credential).subscribe(code=> {
          console.log(code)
          localStorage.setItem("resetCode", code);
        });

    });
  }

  continue(code: string){
    console.log(localStorage.getItem("resetCode"),code)
    if(localStorage.getItem("resetCode")==code) {
      document.getElementById("titlu").innerHTML = "Choose a new password!";
      document.getElementById("reset1").style.display = "none";
      document.getElementById("reset").style.display = "none";
      document.getElementById("resetEmail").style.display = "none";
      document.getElementById("resetPhone").style.display = "none";
      document.getElementById("continue").style.display = "block";
    }else{
      this.userService.sendCode(this.credential.split(".")[0]).subscribe(code=> {
        localStorage.setItem("resetCode", code);
      });
    }
  }

  succes(password: string, passwordConfirm: string){
    if(password==passwordConfirm) {
      if(password.length>=7) {
        this.user.password = password;
        this.userService.changePassword(this.user.id,this.user).subscribe(response=>{
          console.log(response);
        });
        document.getElementById("continue").style.display = "none";
        document.getElementById("succes").style.display = "block";
      }}
    else
      document.getElementById("coincide").innerHTML="The passwords don't coincide."
  }

  isError1(){
    return this.emailControllForm.invalid;
  }
  isError2(){
    return this.phoneControllForm.invalid;
  }
  isError3(){
    return this.codeControlForm.invalid;
  }
  isError4(){
    return this.passwordControlForm.invalid;
  }
}
