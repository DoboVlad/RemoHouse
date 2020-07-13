import { Component, OnInit } from '@angular/core';
import {UserService} from "../../service/userService";
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-validate-email',
  templateUrl: './validate-email.component.html',
  styleUrls: ['./validate-email.component.css']
})
export class ValidateEmailComponent implements OnInit {

  constructor(private userService : UserService) {
    this.validated = false;
<<<<<<< HEAD
    this.userService.sendConfirmationCode(localStorage.getItem("email").split(".")[0]).subscribe(code=>{
=======
    this.userService.sendConfirmationCode(localStorage.getItem("email").split("@")[0]+"@").subscribe(code=>{
>>>>>>> 1c8feeb7ed9989c0e4ccb4ebdec6fbb1c46c38f9
      localStorage.setItem("confirmationCode",code);
    });
  }

  ngOnInit(): void {
  }

  codeControlForm= new FormControl('',[
    Validators.required,
    Validators.minLength(6),
    Validators.maxLength(6)
  ]);
  validated: boolean;

  checkCode(code: string){
    console.log("hi",localStorage.getItem("confirmationCode"),code)
    if(localStorage.getItem("confirmationCode")==code) {
      this.validated = true;
<<<<<<< HEAD
      this.userService.validateAccount(localStorage.getItem("email").split(".")[0]).subscribe(response=>{
        localStorage.clear();
      });
    }else{
      this.userService.sendCode(localStorage.getItem("email").split(".")[0]).subscribe(code=> {
=======
      this.userService.validateAccount(localStorage.getItem("email").split("@")[0]+"@").subscribe(response=>{
        localStorage.clear();
      });
    }else{
      this.userService.sendCode(localStorage.getItem("email").split("@")[0]+"@").subscribe(code=> {
>>>>>>> 1c8feeb7ed9989c0e4ccb4ebdec6fbb1c46c38f9
        localStorage.setItem("confirmationCode", code);
      });
    }
  }
}
