import { Component, OnInit } from '@angular/core';
import {Book} from "../../books/shared/book.model";
import {Client} from "../shared/client.model";
import {ClientService} from "../shared/client.service";

@Component({
  selector: 'app-top-clients',
  templateUrl: './top-clients.component.html',
  styleUrls: ['./top-clients.component.css']
})
export class TopClientsComponent implements OnInit {
  cancel: string='cancel';
  clientList: Client[];

  book: Book;
  clients: Client[];
  client: number;
  constructor(private clientService: ClientService) {
    this.clientService.getFilteredClients().subscribe(clients=>this.clientList=clients);
  }

  ngOnInit(): void {
  }


}
