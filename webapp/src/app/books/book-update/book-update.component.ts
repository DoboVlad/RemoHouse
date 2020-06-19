import {Component, Inject, OnInit} from '@angular/core';
import {BookService} from "../shared/book.service";
import {ActivatedRoute} from "@angular/router";
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {Location} from "@angular/common";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-book-update',
  templateUrl: './book-update.component.html',
  styleUrls: ['./book-update.component.css']
})
export class BookUpdateComponent implements OnInit {

  title:string;
  author:string;
  price:number;
  year:number;
  inStock:number;
  serialNumber:string;

  constructor(public dialogRef: MatDialogRef<BookUpdateComponent>,
              @Inject(MAT_DIALOG_DATA) public id: number,private bookService:BookService, private route: ActivatedRoute,private location: Location) {
    this.bookService.showDetails(this.id).subscribe(book=>{
        this.title=book.title;
        this.author=book.author;
        this.price=book.price;
        this.year=book.year;
        this.inStock=book.inStock;
        this.serialNumber=book.serialNumber;
      }
    )
  }

  ngOnInit(): void {
  }
  onSubmit(form:NgForm){
    console.log("updateBook:",form.value,this.id);
    var newValues = form.value;
    var title = newValues['title'];
    var serialNumber = this.serialNumber;
    var author = newValues['author'];
    var year = newValues['year'];
    var price = newValues['price'];
    var inStock = newValues['instock'];
    this.bookService.updateBook({
      id:this.id,
      serialNumber,
      title,
      author,
      year,
      price,
      inStock,
    }).subscribe(book=>console.log("updated book: ",book));
  }

}
