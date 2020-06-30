import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../service/userService";
import {User} from "../../model/user";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ChangePasswordDialogComponent} from "../change-password-dialog/change-password-dialog.component";

export interface DialogData {
   oldPassword: string;
   newPassword: string;
}
@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  user: User;
  constructor(
    public dialog:MatDialog,
    private router : Router,
    private userService : UserService,
    public snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    var aux=localStorage.getItem("user");
    this.userService.getUserByCredential(aux).subscribe(user=>{
      this.user=user;
    });
  }

  logOut() {
    localStorage.setItem("user",null);
    this.router.navigate(["/home"]);
  }

  checkPassword(password: string):boolean{
    //just to play around
    var rez=true;
    if(password.length<7){
      rez=false;
    }
    return rez;

  }

  changePassword() {
    const dialogRef=this.dialog.open(ChangePasswordDialogComponent,{
      data:{}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      //result can be null on cancel
      if(result!=null) {
        //check if old password is right
        if (result.oldPassword == this.user.password) {
          //check length password (just to be)
          if(!this.checkPassword(result.newPassword))
            this.snackBar.open("The new password is too short.","Ok",{duration:2000});
          else {
            this.user.password = result.newPassword;
            console.log("changing password ",result.newPassword);
            this.userService.changePassword(this.user.id, this.user).subscribe(response=>{
              console.log(response);
              this.snackBar.open("Password changed.","Ok",{duration:2000});
            });
          }
        } else
          this.snackBar.open("The old password is incorrect","Ok",{duration:2000});
      }
    });

  }
}

