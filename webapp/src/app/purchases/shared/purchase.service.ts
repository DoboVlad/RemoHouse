import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {PurchaseIDs} from "./purchase.modelIDs";
@Injectable()
export class PurchaseService{
  private purchaseURL = 'http://localhost:8080/api/purchases';

  constructor(private httpClient: HttpClient) {
  }

  getPurchases(id:number): Observable<Array<PurchaseIDs>>{
    return this.httpClient.get<Array<PurchaseIDs>>(this.purchaseURL+"/"+id);
  }

}
