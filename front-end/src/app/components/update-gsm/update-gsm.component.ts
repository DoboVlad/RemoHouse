import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-update-gsm',
  templateUrl: './update-gsm.component.html',
  styleUrls: ['./update-gsm.component.css']
})
export class UpdateGSMComponent implements OnInit {

  ngOnInit(): void {
  }
  constructor(public dialogRef:MatDialogRef<boolean>) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }
  Update(){

  }
}
