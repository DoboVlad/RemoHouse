import {Component, Inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {UserService} from "../../service/userService";
import {User} from "../../model/user";
import {MatSnackBar} from "@angular/material/snack-bar";

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

  changePassword() {
    const dialogRef=this.dialog.open(ChangePasswordDialog,{
      data:{}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      //result can be null on cancel
      if(result!=null) {
        //check if old password is right
        if (result.oldPassword === this.user.password) {
          //check length password (just to be)
          if(result.newPassword.length<7){
            this.snackBar.open("New password is invalid","Ok",{
              duration:2000
            });
          }
          else {
            this.user.password = result.newPassword;
            this.userService.changePassword(this.user.id, this.user);
          }
        } else {
          this.snackBar.open("Old password is incorrect","Ok",{
            duration:2000
          });
        }
      }
      else {
        this.snackBar.open("Test snackBar", "Ok", {
          duration: 2000
        });
      }
    });

  }
}

@Component({
  selector: 'dialogChangePassword',
  templateUrl: 'dialog.html',
  styleUrls: ['./dialog.css']
})
export class ChangePasswordDialog{
  constructor(public dialogRef:MatDialogRef<ChangePasswordDialog>,@Inject(MAT_DIALOG_DATA) public data:DialogData) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }

  parse(oldPassword: string, newPassword: string) {
    this.data.oldPassword=oldPassword;
    this.data.newPassword=newPassword;
    this.dialogRef.close(this.data)
  }
}
