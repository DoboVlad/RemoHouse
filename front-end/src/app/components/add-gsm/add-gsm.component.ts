import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoginDialogData} from "../account/account.component";
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-add-gsm',
  templateUrl: './add-gsm.component.html',
  styleUrls: ['./add-gsm.component.css']
})
export class AddGSMComponent implements OnInit {

  typeControllForm=new FormControl('',[
    Validators.required,
    Validators.nullValidator,
  ]);

    phoneControllForm= new FormControl('',[
      Validators.required,
      Validators.minLength(10),
      Validators.maxLength(10),
      Validators.pattern(/^(07[0-8]{1}[0-9]{1}|02[0-9]{2}|03[0-9]{2}){1}?([0-9]{3}(\s|\.|\-|)){2}$/)
    ]);

  ngOnInit(): void {
  }
  constructor(public dialogRef:MatDialogRef<AddGSMComponent>,@Inject(MAT_DIALOG_DATA) public data:LoginDialogData) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }
  add(type: string, status: string, phoneNumber:string) {
    console.log(status);
    this.data.gsm_type = type;
    this.data.status=status;
    this.data.phoneNumber=phoneNumber;
    this.dialogRef.close(this.data);
  }
  isError(){
    return this.typeControllForm.invalid || this.phoneControllForm.invalid;
  }
}

