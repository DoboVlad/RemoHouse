import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Client} from "./client.model";
import {Book} from "../../books/shared/book.model";

@Injectable()
export class ClientService{
  private clientURL = 'http://localhost:8080/api/clients';
  private sortURL = "http://localhost:8080/api/sort";

  constructor(private httpClient: HttpClient) {
  }

  getClients(): Observable<Client[]>{
    console.log("all clients");
    return this.httpClient.get<Array<Client>>(this.clientURL);
  }

  saveClient(client: Client): Observable<Client>{
    console.log("saveClient",client);
    return this.httpClient.post<Client>(this.clientURL,client);
  }

  deleteClient(id: number): Observable<Client>{
    console.log("deleteClient",id);
    return this.httpClient.delete<Client>(this.clientURL+"/"+id);
  }
  showDetails(id:number) : Observable<Client>{
    console.log("showDetails",id);
    return this.httpClient.get<Client>(this.clientURL + "/find/" + id);
  }

  updateClient(client: { id:number, serialNumber: string; name: string}): Observable<Client>{
    console.log("update client",client);
    return this.httpClient.put<Client>(this.clientURL+"/"+client.id,client);
  }

  addBookToClient(client:Client, book:Book,date:string): Observable<Client>{
    console.log("add book to client",client,book,date);
    return this.httpClient.put<Client>(this.clientURL+"/purchase/"+client.id+"/"+date,book);
  }

  removeBookFromClient(client: Client, book: Book) : Observable<number>{
    console.log("remove book from client:",book,client);
    return this.httpClient.put<number>(this.clientURL+"/purchase/remove/"+client.id,book);
  }

  getFilteredClients() : Observable<Array<Client>>{
    console.log("getFilteredClients");
    return this.httpClient.get<Array<Client>>(this.clientURL+"/filterpurchases");
  }
}
