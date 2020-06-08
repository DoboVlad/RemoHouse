import { Component, OnInit } from '@angular/core';
import {Book} from "../../books/shared/book.model";
import {Client} from "../../clients/shared/client.model";
import {ClientService} from "../../clients/shared/client.service";
import {MatListOption} from "@angular/material/list";

@Component({
  selector: 'app-update-purchase-client',
  templateUrl: './update-purchase-client.component.html',
  styleUrls: ['./update-purchase-client.component.css']
})
export class UpdatePurchaseClientComponent implements OnInit {
  cancel: string='cancel';
  clientList: Client[];

  book: Book;
  searchText: string;
  clients: Client[];
  client: number;
  selectedOptions: number[];
  constructor(private clientService: ClientService) {
    this.clientService.getClients().subscribe(c=>this.clientList=c);
  }

  ngOnInit(): void {
  }

  onNgModelChange(event,selection:MatListOption[]){
    if (selection.length <= 1) {
    } else {
      selection.reverse();
      selection.pop();
    }
    console.log('on ng model change', selection.map(o=>o.value));
    this.selectedOptions = selection.map(o=>o.value);
    this.client = this.selectedOptions[0];
  }

}
