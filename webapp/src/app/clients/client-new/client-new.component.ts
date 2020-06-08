import { Component, OnInit } from '@angular/core';
import {ClientService} from "../shared/client.service";
import {Location} from "@angular/common";

@Component({
  selector: 'app-client-new',
  templateUrl: './client-new.component.html',
  styleUrls: ['./client-new.component.css']
})
export class ClientNewComponent implements OnInit {

  constructor(private clientService:ClientService, private location:Location) { }

  ngOnInit(): void {
  }

  saveClient(serialNumber: string, name: string) {
    console.log("saving client", serialNumber, name);

    this.clientService.saveClient({
      id: 0,
      serialNumber,
      name,
      books:[]
    })
      .subscribe(client => console.log("saved client: ", client));

  }

}
