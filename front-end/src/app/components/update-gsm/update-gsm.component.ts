import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {LoginDialogData} from "../account/account.component";

@Component({
  selector: 'app-update-gsm',
  templateUrl: './update-gsm.component.html',
  styleUrls: ['./update-gsm.component.css']
})
export class UpdateGSMComponent implements OnInit {

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
