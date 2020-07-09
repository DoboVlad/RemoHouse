import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-delete-room',
  templateUrl: './delete-room.component.html',
  styleUrls: ['./delete-room.component.css']
})
export class DeleteRoomComponent implements OnInit {

  constructor(public dialogRef:MatDialogRef<DeleteRoomComponent>) { }
  closeDialog(){
    this.dialogRef.close(false);
  }
  deleteRoom(){
    this.dialogRef.close(true);

  }

  ngOnInit(): void {
  }

}
