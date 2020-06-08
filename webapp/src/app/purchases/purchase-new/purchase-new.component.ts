import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {PurchaseService} from "../shared/purchase.service";
import {BookService} from "../../books/shared/book.service";
import {ClientService} from "../../clients/shared/client.service";
import {Location} from "@angular/common";
import {Book} from "../../books/shared/book.model";
import {Client} from "../../clients/shared/client.model";
import {MatListOption} from "@angular/material/list";

@Component({
  selector: 'app-purchase-new',
  templateUrl: './purchase-new.component.html',
  styleUrls: ['./purchase-new.component.css']
})
export class PurchaseNewComponent implements OnInit {
  book: Book;
  searchText: string;
  clients: Client[];
  client: Client;
  p: number = 1;
  bookList: Book[];
  selectedOptions: number[];
  constructor(private books:BookService, private clientService: ClientService, private location:Location, private purchaseService:PurchaseService,private route: ActivatedRoute) {
    this.books.getBooks().subscribe(b=>this.bookList=b);
    console.log('in constr');
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.clientService.showDetails(id).subscribe(b=> this.client=b);

    console.log(this.clients);
  }

  buyBook() {
    if(this.selectedOptions.length==0)
      alert("Book not selected");
    if(this.selectedOptions.length>1)
      alert("Please select only one book")
    this.books.showDetails(this.selectedOptions[0])
      .subscribe(book=> {
        var c = this.client;
        this.clientService.addBookToClient(c,book,"a").subscribe(a=>
          this.books.addClientToBook(c,book,"").subscribe(b=>
            console.log("book bought ",a,b))
        );
      }
  );
  }

  onNgModelChange(event,selection:MatListOption[]){
    console.log('on ng model change', selection.map(o=>o.value));
    this.selectedOptions = selection.map(o=>o.value);
  }

}
