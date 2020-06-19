import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {BookNewComponent} from "./book-new/book-new.component";
import {TopClientsComponent} from "../clients/top-clients/top-clients.component";
import {TopBooksComponent} from "./top-books/top-books.component";
import {BookService} from "./shared/book.service";

@Component({
  selector: 'app-books',
  template: '<button (click)="addNewBook()">Add new book </button>' +
    '<button (click)="seeTopBooks()">Top books</button>' +
    '<button (click)="seeTotalStock()">Total stock</button>' +
    '<app-books-list></app-books-list>\n'    ,
  styleUrls: ['./books.component.css']
})
export class BooksComponent implements OnInit {
  constructor(private dialog:MatDialog,private router: Router,private bookService: BookService) { }

  ngOnInit(): void {
  }

  addNewBook(){
    console.log("add new book button clicked");
    this.router.navigate(["book/new"]);
    //this.openDialog();
  }
  private openDialog(): void {
    const dialogRef = this.dialog.open(BookNewComponent, {
      width: '20em'
    });
    dialogRef.afterClosed().subscribe(c=>window.location.reload());

  }

  seeTopBooks() {
    console.log("see top books button clicked");
    this.openTopDialog();
  }

  private openTopDialog() {
    const dialogRef = this.dialog.open(TopBooksComponent, {
      width: '20em'
    });
  }

  seeTotalStock() {
    console.log("see total stock of books button clicked");
    this.bookService.getTotalStock().subscribe(stock=>{
      alert("Total stock is: "+stock);
    });
  }

}
