import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DialogData} from "../account/account.component";

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.css']
})
export class ChangePasswordDialogComponent implements OnInit {

  constructor(public dialogRef:MatDialogRef<ChangePasswordDialogComponent>,@Inject(MAT_DIALOG_DATA) public data:DialogData) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }

  parse(oldPassword: string, newPassword: string) {
    this.data.oldPassword=oldPassword;
    this.data.newPassword=newPassword;
    this.dialogRef.close(this.data)
  }

  ngOnInit(): void {
  }

}
