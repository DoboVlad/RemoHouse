import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  constructor() {
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

  reset(c) {
    document.getElementById("titlu").innerHTML = "Please enter your code below!";
    document.getElementById("reset1").style.display = "block";
    document.getElementById("reset").style.display = "none";
    document.getElementById("resetEmail").style.display = "none";
    document.getElementById("resetPhone").style.display = "none";
    document.getElementById("span1").innerHTML=c;
  }
  continue(){
    document.getElementById("titlu").innerHTML = "Choose a new password!";
    document.getElementById("reset1").style.display = "none";
    document.getElementById("reset").style.display = "none";
    document.getElementById("resetEmail").style.display = "none";
    document.getElementById("resetPhone").style.display = "none";
    document.getElementById("continue").style.display="block";
  }
}
