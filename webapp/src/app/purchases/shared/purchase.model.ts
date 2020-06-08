import {Client} from "../../clients/shared/client.model";
import {Book} from "../../books/shared/book.model";

//one client and a list of books for that client
export class Purchase{
  client: Client;
  book: Book;
  date: string;
  constructor(client: Client, book: Book,date:string) {
    this.client = client;
    this.book = book;
    this.date = date;
  }
}
