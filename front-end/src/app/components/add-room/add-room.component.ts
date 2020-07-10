import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-room',
  templateUrl: './add-room.component.html',
  styleUrls: ['./add-room.component.css']
})
export class AddRoomComponent implements OnInit {

  constructor(public dialogRef:MatDialogRef<AddRoomComponent>) { }
  closeDialog(){
    this.dialogRef.close(null);
  }
  add(name: string){
    this.dialogRef.close(name);
  }

  ngOnInit(): void {
  }

}
