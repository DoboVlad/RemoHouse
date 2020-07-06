import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-delete-button-dialog',
  templateUrl: './delete-button-dialog.component.html',
  styleUrls: ['./delete-button-dialog.component.css']
})
export class DeleteButtonDialogComponent implements OnInit {

  constructor(public dialogRef:MatDialogRef<DeleteButtonDialogComponent>) { }
  close(){
    this.dialogRef.close(false);
  }
  delete(){
    this.dialogRef.close(true);
}
  ngOnInit(): void {
  }

}
