import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {

  }
  resetPassword(){
    document.getElementById("titlu").innerHTML="You can reset your password here!";
    document.getElementById("reset").style.display="none";

    document.getElementById("resetPhone").style.display="inline-block";
  }
  resetPassword1(){
    document.getElementById("titlu").innerHTML="You can reset your password here!";
    document.getElementById("reset").style.display="none";

    document.getElementById("resetEmail").style.display="inline-block";
  }
  cancel(){
    document.getElementById("titlu").innerHTML="How would you like to reset your password?";
    document.getElementById("reset").style.display="inline";

    document.getElementById("resetEmail").style.display="none";
    document.getElementById("resetPhone").style.display="none";
  }
}
