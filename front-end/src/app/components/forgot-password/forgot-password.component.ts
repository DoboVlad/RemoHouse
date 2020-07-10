import { Component, OnInit } from '@angular/core';
import {UserService} from "../../service/userService";
import {User} from "../../model/user";

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
      if (result.name == null)
        if(c=="email")
          alert("This e-mail does not belong to any user.");
        else
          alert("This phone number does not belong to any user");
      else {
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
      }
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
      alert("Incorrect code. A new one is being sent.")
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
      }
      else
        alert("Password is too short.");
    }else
      alert("The passwords don't coincide.");
  }
}
