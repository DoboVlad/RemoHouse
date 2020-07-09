import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoginDialogData} from "../account/account.component";

@Component({
  selector: 'app-add-gsm',
  templateUrl: './add-gsm.component.html',
  styleUrls: ['./add-gsm.component.css']
})
export class AddGSMComponent implements OnInit {


  ngOnInit(): void {
  }
  constructor(public dialogRef:MatDialogRef<AddGSMComponent>,@Inject(MAT_DIALOG_DATA) public data:LoginDialogData) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }
  add(type: string, status: string, phoneNumber:string) {
    this.data.gsm_type = type;
    this.data.status=status;
    this.data.phoneNumber=phoneNumber;
    this.dialogRef.close(this.data);
  }
}

