import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-update-room',
  templateUrl: './update-room.component.html',
  styleUrls: ['./update-room.component.css']
})
export class UpdateRoomComponent implements OnInit {
  nameControllForm=new FormControl('',[
    Validators.required,
    Validators.nullValidator
  ]);

  isError(){
    return this.nameControllForm.invalid
  }

  constructor(public dialogRef: MatDialogRef<UpdateRoomComponent>) { }

  closeDialog(){
    this.dialogRef.close(null);
  }
  save(name: string){
    this.dialogRef.close(name);

  }
  ngOnInit(): void {
  }

}
