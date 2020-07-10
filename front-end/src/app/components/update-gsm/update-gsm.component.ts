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
    Validators.nullValidator,
    Validators.pattern("^-?[0-9]{1,10}(?:\\.[0-9]{1,10})?$")
  ])
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
