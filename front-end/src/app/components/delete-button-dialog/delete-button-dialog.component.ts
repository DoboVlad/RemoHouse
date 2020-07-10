import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {Router} from "@angular/router";

@Component({
  selector: 'app-delete-button-dialog',
  templateUrl: './delete-button-dialog.component.html',
  styleUrls: ['./delete-button-dialog.component.css']
})
export class DeleteButtonDialogComponent implements OnInit {

  constructor(public dialogRef:MatDialogRef<DeleteButtonDialogComponent>, private router:Router) {
      if (localStorage.getItem("user") == "null") {
        this.router.navigate(["/unauthorizedaccess"]);
      }
  }
  close(){
    this.dialogRef.close(false);
  }
  delete(){
    this.dialogRef.close(true);
}
  ngOnInit(): void {
  }

}
