import {Client} from "../../clients/shared/client.model";

export class Book{
  id: number;
  serialNumber: string;
  title: string;
  author: string;
  year: number;
  price: number;
  inStock: number;
  clients: Client[]
}
