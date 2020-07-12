import { Component, OnInit } from '@angular/core';
import {User} from "../../model/user";
import {UserService} from "../../service/userService";
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-validate-account',
  templateUrl: './validate-account.component.html',
  styleUrls: ['./validate-account.component.css']
})
export class ValidateAccountComponent implements OnInit {
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

  cancel() {
    document.getElementById("titlu").innerHTML = "How would you like to reset your password?";
    document.getElementById("reset").style.display = "inline";
    document.getElementById("resetEmail").style.display = "none";
    document.getElementById("resetPhone").style.display = "none";
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

  isError3(){
    return this.codeControlForm.invalid;
  }
}
