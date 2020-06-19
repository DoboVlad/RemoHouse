import {Component, OnInit, ViewChild} from '@angular/core';
import {Client} from "../shared/client.model";
import {ClientService} from "../shared/client.service";
import {MatTable, MatTableDataSource} from "@angular/material/table";
import {SelectionModel} from "@angular/cdk/collections";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {UpdatePurchaseBookComponent} from "../../purchases/update-purchase-book/update-purchase-book.component";
import {BookService} from "../../books/shared/book.service";
import {ClientUpdateComponent} from "../client-update/client-update.component";

@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {

  clients: Client[];
  bookID:number;
  p: number = 1;
  resultsLength = 0;
  displayedColumns: string[] = ['select','serialNumber','name'];
  dataSource : MatTableDataSource<Client>;
  selection = new SelectionModel<Client>(true, []);

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatTable,{static:true}) table: MatTable<Client>;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private dialog:MatDialog, private clientService: ClientService, private bookService:BookService) {
    this.dataSource =  new MatTableDataSource();
    this.dataSource.sort = this.sort;
    this.clientService.getClients().subscribe(clients=>{
      this.clients=clients;
      this.dataSource.data = this.clients;
      this.resultsLength = this.clients.length
      this.dataSource.paginator = this.paginator;
    });
  }

  ngOnInit(): void {

  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  deleteClient(){
    console.log("deleting client");
    if(this.selection.selected.length==0)
      alert("Select at least one client to be deleted");
    else{
      this.selection.selected.forEach((client)=>{
        var id = client.id;
        this.clients.filter(c=>c!=client);
        this.clientService.deleteClient(id)
          .subscribe(c=>{
            console.log("deleted client: ",c);
            this.dataSource.data = this.clients;
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
  checkboxLabel(row?: Client): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.name + 1}`;
  }

  buy(){
    if(this.selection.selected.length==0)
      alert("Please select one client.");
    if(this.selection.selected.length!=1)
      alert("You can add a purchase to one customer at a time");
    else{
      this.openDialog();
    }
  }
  private openDialog(): void {
    const dialogRef = this.dialog.open(UpdatePurchaseBookComponent, {
      width: '30em'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.bookID = result;
      console.log(result);
      if(this.bookID==-1)
        return;
      else
        this.buyBook();
    });
  }

  private buyBook() {
    var clientID = this.selection.selected[0].id;
    this.bookService.showDetails(this.bookID)
      .subscribe(book=> {
        this.clientService.showDetails(clientID).subscribe(client => {
          this.clientService.addBookToClient(client, book,"a").subscribe(a =>
              console.log("book bought "));});
      });
  }

  updateClient() {
    if(this.selection.selected.length==0)
      alert("Please select one client.");
    if(this.selection.selected.length!=1)
      alert("You can update a client at a time");
    else{
      this.openDialogUpdate(this.selection.selected[0].id);
    }
  }
  private openDialogUpdate(id:number): void {
    const dialogRef = this.dialog.open(ClientUpdateComponent, {
      width: '20em',
      data:id
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      this.table.renderRows();
      window.location.reload();
    });
  }
}
