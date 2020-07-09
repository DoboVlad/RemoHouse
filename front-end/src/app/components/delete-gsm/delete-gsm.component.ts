import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {LoginDialogData} from "../account/account.component";

@Component({
  selector: 'app-delete-gsm',
  templateUrl: './delete-gsm.component.html',
  styleUrls: ['./delete-gsm.component.css']
})
export class DeleteGSMComponent implements OnInit {

  constructor(public dialogRef:MatDialogRef<DeleteGSMComponent>,@Inject(MAT_DIALOG_DATA) public data:LoginDialogData) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }
  ngOnInit(): void {
  }
  DeleteGSM(){
    this.dialogRef.close(this.data);
  }
}
