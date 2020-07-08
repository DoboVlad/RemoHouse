import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-delete-gsm',
  templateUrl: './delete-gsm.component.html',
  styleUrls: ['./delete-gsm.component.css']
})
export class DeleteGSMComponent implements OnInit {

  constructor(public dialogRef:MatDialogRef<boolean>) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }
  ngOnInit(): void {
  }

}
