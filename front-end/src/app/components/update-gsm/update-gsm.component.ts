import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {LoginDialogData} from "../account/account.component";
import {FormControl, Validators} from "@angular/forms";

@Component({
  selector: 'app-update-gsm',
  templateUrl: './update-gsm.component.html',
  styleUrls: ['./update-gsm.component.css']
})
export class UpdateGSMComponent implements OnInit {

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

  isError(){
    return this.typeControllForm.invalid || this.phoneControllForm.invalid;
  }
  ngOnInit(): void {
  }
  constructor(public dialogRef:MatDialogRef<UpdateGSMComponent>,@Inject(MAT_DIALOG_DATA) public data:LoginDialogData) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }
  Update(gsm_type: string, status:string, phoneNumber: string){
    console.log(gsm_type);
      this.data.gsm_type=gsm_type;
      this.data.status=status;
      this.data.phoneNumber=phoneNumber;
      this.dialogRef.close(this.data);
  }
}
