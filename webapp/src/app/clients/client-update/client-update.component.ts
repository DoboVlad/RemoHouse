import {Component, Inject, OnInit} from '@angular/core';
import {ClientService} from "../shared/client.service";
import {ActivatedRoute} from "@angular/router";
import {NgForm} from "@angular/forms";
import {Location} from "@angular/common";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BookUpdateComponent} from "../../books/book-update/book-update.component";

@Component({
  selector: 'app-client-update',
  templateUrl: './client-update.component.html',
  styleUrls: ['./client-update.component.css']
})
export class ClientUpdateComponent implements OnInit {

  serialNumber:string;
  name:string;
  constructor(public dialogRef: MatDialogRef<BookUpdateComponent>,
              @Inject(MAT_DIALOG_DATA) public id: number,
              private clientService:ClientService, private route: ActivatedRoute,private location: Location) { }

  ngOnInit(): void {
    this.clientService.showDetails(this.id).subscribe(c=>{
        this.name=c.name;
        this.serialNumber=c.serialNumber;
      }
    )
  }

  onSubmit(form:NgForm){
    console.log("updateClient",form.value);
    var newValues = form.value;
    var name = newValues['name'];
    var serialNumber = newValues['serialNumber'];
    this.clientService.updateClient({
      id:this.id,
      serialNumber,
      name
    }).subscribe(c=>console.log("updated client: ",c));
  }

}


