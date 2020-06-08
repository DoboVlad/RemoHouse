import {Book} from "../../books/shared/book.model";

export class Client{
  id: number;
  serialNumber: string;
  name: string;
  books: Book[]
}
