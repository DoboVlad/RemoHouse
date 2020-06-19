import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-dialog-update',
  templateUrl: './dialog-update.component.html',
  styleUrls: ['./dialog-update.component.css']
})
export class DialogUpdateComponent implements OnInit {

  constructor() { }
  public dialogRef: MatDialogRef<DialogUpdateComponent>;
  client: string = 'client';
  book: string = 'book';
  cancel: string='cancel';
  ngOnInit(): void {
  }

}
