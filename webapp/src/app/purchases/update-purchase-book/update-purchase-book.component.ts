import { Component, OnInit } from '@angular/core';
import {MatListOption} from "@angular/material/list";
import {Book} from "../../books/shared/book.model";
import {BookService} from "../../books/shared/book.service";

@Component({
  selector: 'app-update-purchase-book',
  templateUrl: './update-purchase-book.component.html',
  styleUrls: ['./update-purchase-book.component.css']
})
export class UpdatePurchaseBookComponent implements OnInit {
  searchText: string;
  selectedOptions: number[];
  bookList: Book[];
  book: number;
  cancel: number=-1;

  constructor(private bookService:BookService) {
    this.bookService.getBooks().subscribe(books=>this.bookList=books);
  }

  ngOnInit(): void {
  }

  onNgModelChange($event: any, selection: MatListOption[]) {
    if(selection.length>1) {
      selection.reverse();
      selection.pop();
    }
    console.log('on ng model change', selection.map(o=>o.value));
    this.selectedOptions = selection.map(o=>o.value);
    this.book = this.selectedOptions[0];
  }
}
