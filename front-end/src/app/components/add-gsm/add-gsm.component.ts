import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {DialogData} from "../account/account.component";

@Component({
  selector: 'app-add-gsm',
  templateUrl: './add-gsm.component.html',
  styleUrls: ['./add-gsm.component.css']
})
export class AddGSMComponent implements OnInit {


  ngOnInit(): void {
  }
  constructor(public dialogRef:MatDialogRef<boolean>) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }
}

