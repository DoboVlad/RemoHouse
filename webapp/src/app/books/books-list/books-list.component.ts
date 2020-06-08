import {Component, OnInit, ViewChild} from '@angular/core';
import {BookService} from "../shared/book.service";
import {Book} from "../shared/book.model";
import {Location} from "@angular/common";
import {Router} from "@angular/router";
import {Client} from "../../clients/shared/client.model";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {SelectionModel} from "@angular/cdk/collections";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {UpdatePurchaseBookComponent} from "../../purchases/update-purchase-book/update-purchase-book.component";
import {BookUpdateComponent} from "../book-update/book-update.component";

@Component({
  selector: 'app-books-list',
  styleUrls: ['./books-list.component.css'],
  template: `<h2>Books</h2>
  <mat-form-field>
    <mat-label>Filter after title</mat-label>
    <label>
      <input matInput (keyup)="applyFilter($event)" placeholder="Ex. em">
    </label>
  </mat-form-field>

  <table mat-table #table [dataSource]="dataSource" matSort class="mat-elevation-z8">
    <!-- Checkbox Column -->
    <ng-container matColumnDef="select">
      <th mat-header-cell *matHeaderCellDef>
        <mat-checkbox (change)="$event ? masterToggle() : null"
                      [checked]="selection.hasValue() && isAllSelected()"
                      [indeterminate]="selection.hasValue() && !isAllSelected()"
                      [aria-label]="checkboxLabel()">
        </mat-checkbox>
      </th>
      <td mat-cell *matCellDef="let row">
        <mat-checkbox (click)="$event.stopPropagation()"
                      (change)="$event ? selection.toggle(row) : null"
                      [checked]="selection.isSelected(row)"
                      [aria-label]="checkboxLabel(row)">
        </mat-checkbox>
      </td>
    </ng-container>
    <!-- Title Column -->
    <ng-container matColumnDef="title">
      <th mat-header-cell *matHeaderCellDef mat-sort-header> Title </th>
      <td mat-cell *matCellDef="let element"> {{element.title}} </td>
    </ng-container>

    <!-- Author Column -->
    <ng-container matColumnDef="author">
      <th mat-header-cell *matHeaderCellDef mat-sort-header> Author </th>
      <td mat-cell *matCellDef="let element"> {{element.author}} </td>
    </ng-container>

    <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
    <tr mat-row *matRowDef="let row; columns: displayedColumns;"
        (click)="selection.toggle(row)">
    </tr>
  </table>
  <mat-paginator [length]="resultsLength" [pageSize]="10" [pageSizeOptions]="[3, 5, 10, 25]"></mat-paginator>

  <button class="delete" (click)="deleteBook()" >
    Delete
  </button>
  <button class="buybtn" (click)="updateBook()">
    Update
  </button>

  `
})
export class BooksListComponent implements OnInit {
  books: Book[];
  p: number = 1;

  resultsLength = 0;
  displayedColumns: string[] = ['select','title','author'];
  dataSource : MatTableDataSource<Book>;
  selection = new SelectionModel<Book>(true, []);

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatTable,{static:true}) table: MatTable<Book>;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private dialog:MatDialog,private booksService: BookService,private location: Location,private router: Router) {
    this.dataSource =  new MatTableDataSource();
    this.dataSource.sort = this.sort;
    this.booksService.getBooks().subscribe(books=>{
      this.books=books;
      this.dataSource.data = this.books;
      this.resultsLength = this.books.length
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort=this.sort;
    });
  }

  ngOnInit(): void {
  }

  deleteBook(){
    console.log("deleting book");
    if(this.selection.selected.length==0)
      alert("Select at least one book to be deleted");
    else{
      this.selection.selected.forEach((book)=>{
        var id = book.id;
        this.booksService.deleteBook(id)
          .subscribe(c=>{
            console.log("deleted book: ",c);
            this.books.filter(c=>c!=book);
            this.dataSource.data = this.books;
            this.selection.selected.pop();
            this.table.renderRows();
            window.location.reload();
          });
      });
    }
  }
  applyFilter($event: KeyboardEvent) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  masterToggle() {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.data.forEach(row => this.selection.select(row));
  }

  /*** The label for the checkbox on the passed row */
  checkboxLabel(row?: Book): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.title + 1}`;
  }

  updateBook() {
    if(this.selection.selected.length==0)
      alert("Please select one book.");
    if(this.selection.selected.length!=1)
      alert("You can updatea book at a time");
    else{
      this.openDialog(this.selection.selected[0].id);
    }
  }
  private openDialog(id:number): void {
    const dialogRef = this.dialog.open(BookUpdateComponent, {
      width: '100%',
      data:id
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.table.renderRows();
      window.location.reload();
    });
  }
}
