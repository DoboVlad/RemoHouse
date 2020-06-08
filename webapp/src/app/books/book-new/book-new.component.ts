import { Component, OnInit } from '@angular/core';
import {BookService} from "../shared/book.service";
import {Location} from "@angular/common";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-book-new',
  templateUrl: './book-new.component.html',
  styleUrls: ['./book-new.component.css']
})
export class BookNewComponent implements OnInit {
  registerForm: FormGroup;
  submitted = false;
  constructor(private formBuilder: FormBuilder,private bookService: BookService, private location: Location) { }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      sn:['',[Validators.required]],
      title: ['', Validators.required],
      author: ['', Validators.required],
      year: ['', [Validators.required]],
      price: ['', [Validators.required]],
      instock: ['',[Validators.required]]
    });
  }
  get f() { return this.registerForm.controls; }

  saveBook(serialNumber:string, title:string, author: string, year:string, price:string, inStock:string){
    console.log("saving book", serialNumber,title,author,year,price,inStock);
    this.bookService.saveBook({
      id:0,
      serialNumber,
      title,
      author,
      year,
      price,
      inStock,
      clients: []
    }).subscribe(book=>{
      console.log("saved book: ",book);
      this.location.back();
      }
    );
  }
  onSubmit() {
    this.submitted = true;
    if (this.registerForm.invalid) {
      return;
    }
    var newValues = this.registerForm.value;
    var title = newValues['title'];
    var serialNumber = newValues['sn'];
    var author = newValues['author'];
    var year = newValues['year'];
    var price = newValues['price'];
    var inStock = newValues['instock'];
    this.saveBook(serialNumber,title,author,year,price,inStock);
  }
}
