import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-update-room',
  templateUrl: './update-room.component.html',
  styleUrls: ['./update-room.component.css']
})
export class UpdateRoomComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<UpdateRoomComponent>) { }

  closeDialog(){
    this.dialogRef.close(null);
  }
  ngOnInit(): void {
  }

}
