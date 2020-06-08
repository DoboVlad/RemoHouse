import {Component, OnInit, ViewChild} from '@angular/core';
import {PurchaseService} from "../shared/purchase.service";
import {ClientService} from "../../clients/shared/client.service";
import {Purchase} from "../shared/purchase.model";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {MatSort} from "@angular/material/sort";
import {SelectionModel} from "@angular/cdk/collections";
import {BookService} from "../../books/shared/book.service";
import {MatDialog} from "@angular/material/dialog";
import {DialogUpdateComponent} from "../dialog-update/dialog-update.component";
import {UpdatePurchaseBookComponent} from "../update-purchase-book/update-purchase-book.component";
import {UpdatePurchaseClientComponent} from "../update-purchase-client/update-purchase-client.component";

@Component({
  selector: 'app-purchase-list',
  templateUrl: './purchase-list.component.html',
  styleUrls: ['./purchase-list.component.css']
})
export class PurchaseListComponent implements OnInit {

  purchases : Purchase[];
  choice: string;
  clientID: number;
  bookID : number;
  displayedColumns: string[] = ['select','name','title','date'];
  dataSource : MatTableDataSource<Purchase>;
  selection = new SelectionModel<Purchase>(true, []);
  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatTable,{static:true}) table: MatTable<Purchase>;

  constructor(private dialog:MatDialog,private purchaseService: PurchaseService,private clientService: ClientService, private bookService:BookService) {
    this.clientService.getClients().subscribe(clients=>{
      this.dataSource =  new MatTableDataSource();
      this.dataSource.sort = this.sort;
      this.purchases = [];
      clients.forEach((client)=>
        this.purchaseService.getPurchases(client.id)
          .subscribe(purchases =>{
            if(purchases.length!=0) {
              purchases.forEach((purchaseIDs) => {
                console.log(purchaseIDs);
                bookService.showDetails(purchaseIDs.bookID).subscribe(book=>{
                  var purchase = new Purchase(client,book,purchaseIDs.date);
                  this.purchases.push(purchase);
                  this.dataSource.data = this.purchases;
                })
              });
            }
          }));
    });

  }

  ngOnInit(): void {
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

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: Purchase): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.book.title + 1}`;
  }

  deletePurchase() {
    console.log("delete purchases: nr:",this.selection.selected.length);
    console.log(this.selection.selected);
    if(this.selection.selected.length==0)
      alert("Please select at least one purchase to delete.");
    else {
      this.selection.selected.forEach((purchase) => {
        this.purchases = this.purchases.filter(p => p != purchase);
        var client = purchase.client;
        var book = purchase.book;
        this.clientService.removeBookFromClient(client, book).subscribe(
          b => {
              if(b <= 0)
                alert("failed to delete purchase!");
              else {
                console.log("purchase removed");
                this.dataSource.data = this.purchases;
                this.table.renderRows();
                window.location.reload();
              }
            });
      });
    }
  }

  updatePurchase() {
    console.log("update purchase");
    if(this.selection.selected.length==0)
      alert("Select one purchase to update");
    else{
      if(this.selection.selected.length!=1)
        alert("You can update one purchase at a time");
      else{
        this.openDialog();
      }
    }
  }

  private openDialog(): void {
    const dialogRef = this.dialog.open(DialogUpdateComponent, {
      width: '20em'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.choice = result;
      console.log(result);
      if(this.choice=='cancel')
        return;
      if(this.choice=='client'){
        this.openClientUpdateDialog();
      }
      if(this.choice=='book'){
        this.openBookUpdateDialog();
      }
    });
  }

  private openClientUpdateDialog() :void {
    console.log("Open client dialog");
    const dialogRef = this.dialog.open(UpdatePurchaseClientComponent, {
      width: '20em'
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.clientID = result;
      console.log(result);
      if(result!='cancel')
        this.updateClientPurchase();
    });
  }

  private updateClientPurchase() {
    console.log("update purchase client");
    var purchase = this.selection.selected[0];
    this.clientService.showDetails(this.clientID).subscribe(clientToAdd=>{
        this.clientService.removeBookFromClient(purchase.client,purchase.book,).subscribe(rowsAffected=>{
            if(rowsAffected<=0)
              alert("Purchase update failed!");
            else
              this.clientService.addBookToClient(clientToAdd,purchase.book,purchase.date).subscribe(book2=>{
                purchase.client = clientToAdd;
                this.dataSource.data = this.purchases;
                this.selection.selected.pop();
                this.table.renderRows();
                window.location.reload();
              });
          });
      });
  }

  private openBookUpdateDialog() {
    console.log("Open book dialog");
    const dialogRef = this.dialog.open(UpdatePurchaseBookComponent, {
      width: '30em'
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.bookID = result;
      console.log(result);
      if(result!='cancel')
        this.updateBookPurchase();
    });
  }

  private updateBookPurchase() {
    console.log("update purchase book");
    var purchase = this.selection.selected[0];
    this.bookService.showDetails(this.bookID).subscribe(bookToAdd=>{
      this.clientService.removeBookFromClient(purchase.client,purchase.book).subscribe(rowsAffected=>{
        if(rowsAffected<=0)
          alert("Purchase update failed!");
        else
          this.clientService.addBookToClient(purchase.client,bookToAdd,purchase.date).subscribe(client2=>{
            console.log(client2,purchase.book,bookToAdd);
            purchase.book = bookToAdd;
            this.dataSource.data = this.purchases;
            this.selection.selected.pop();
            this.table.renderRows();
            window.location.reload();
          });
      });
    });
  }
}
