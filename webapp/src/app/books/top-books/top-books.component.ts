import { Component, OnInit } from '@angular/core';
import {Book} from "../shared/book.model";
import {BookService} from "../shared/book.service";

@Component({
  selector: 'app-top-books',
  templateUrl: './top-books.component.html',
  styleUrls: ['./top-books.component.css']
})
export class TopBooksComponent implements OnInit {
  bookList: Book[];
  book: number;
  cancel: number=-1;

  constructor(private bookService:BookService) {
    this.bookService.getTopBooks().subscribe(books=>this.bookList=books);
  }

  ngOnInit(): void {
  }

}
