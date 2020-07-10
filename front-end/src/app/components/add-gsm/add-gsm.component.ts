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
      Validators.nullValidator,
      Validators.pattern("^-?[0-9]{1,10}(?:\\.[0-9]{1,10})?$")
    ])
  ngOnInit(): void {
  }
  constructor(public dialogRef:MatDialogRef<AddGSMComponent>,@Inject(MAT_DIALOG_DATA) public data:LoginDialogData) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }
  add(type: string, status: string, phoneNumber:string) {
    console.log(type);
    this.data.gsm_type = type;
    this.data.status=status;
    this.data.phoneNumber=phoneNumber;
    this.dialogRef.close(this.data);
  }
  isError(){
    return this.typeControllForm.invalid || this.phoneControllForm.invalid;
  }
}

