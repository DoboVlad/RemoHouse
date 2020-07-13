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
    this.userService.sendConfirmationCode(localStorage.getItem("email").split("@")[0]+"@").subscribe(code=>{
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
      this.userService.validateAccount(localStorage.getItem("email").split("@")[0]+"@").subscribe(response=>{
        localStorage.clear();
      });
    }else{
      this.userService.sendCode(localStorage.getItem("email").split("@")[0]+"@").subscribe(code=> {
        localStorage.setItem("confirmationCode", code);
      });
    }
  }
}
