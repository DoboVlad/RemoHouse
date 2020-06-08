
export class PurchaseIDs{
  clientID: number;
  bookID: number;
  date: string;
  constructor(client: number, book: number,date:string) {
    this.clientID = client;
    this.bookID = book;
    this.date = date;
  }
}
