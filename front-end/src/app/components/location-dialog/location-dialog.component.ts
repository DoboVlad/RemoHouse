import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {LoginDialogData} from "../account/account.component";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-location-dialog',
  templateUrl: './location-dialog.component.html',
  styleUrls: ['./location-dialog.component.css']
})
export class LocationDialogComponent implements OnInit {


   nameControllForm=new FormControl('',[
    Validators.required,
    Validators.minLength(2),
    Validators.nullValidator,
     Validators.pattern(/^[A-Za-z][A-Za-z'\-]+/)
  ]);
  cityControllForm=new FormControl('',[
    Validators.required,
    Validators.minLength(2),
    Validators.nullValidator,
    Validators.pattern(/^[A-Za-z][A-Za-z'\-]+/)
  ]);
  latitudeControllForm=new FormControl('',
    [
      Validators.required,
      Validators.nullValidator,
      Validators.pattern("^-?[0-9]{1,3}(?:\\.[0-9]{1,10})?$")
    ]);
  longitudeControllForm=new FormControl('',
    [
    Validators.required,
    Validators.nullValidator,
    Validators.pattern("^-?[0-9]{1,3}(?:\\.[0-9]{1,10})?$")
  ]);


  isError(){
    return this.nameControllForm.invalid || this.cityControllForm.invalid || this.latitudeControllForm.invalid || this.longitudeControllForm.invalid;
  }

  constructor(public dialogRef:MatDialogRef<LocationDialogComponent>,@Inject(MAT_DIALOG_DATA) public data:LoginDialogData) {}
  onCloseClick() {
    this.dialogRef.close(null);
  }

  error: String;
  ngOnInit(): void {
  }

  isAdd(){
    return this.data.type==="Add";
  }

  isUpdate(){
    return this.data.type==="Update";
  }

  isDelete(){
    return this.data.type==="Delete";
  }

  add(name: string, latitude: string, longitude: string, city: string) {
      this.data.name = name;
      this.data.latitude = latitude;
      this.data.longitude = longitude;
      this.data.city = city;
      this.dialogRef.close(this.data);

  }


  update(name: string, latitude: string, longitude: string, city: string) {

      this.data.name = name;
      this.data.latitude = latitude;
      this.data.longitude = longitude;
      this.data.city = city;
      this.dialogRef.close(this.data);

  }

  delete() {
    //just be not null
    this.dialogRef.close(this.data);
  }
}
