import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {ActionLogGSM} from "../model/ActionLogGSM";

@Injectable({providedIn: 'root'})
export class ActionLogGSMService {

  private url = 'http://localhost:8080/api/actionLogGSM';

  constructor(private http: HttpClient) {
  }
  private httpSecurity = {
    headers: new HttpHeaders({
      Authorization : `Basic ${window.btoa('user:password')}`
    })
  };

  getActions(userID : number) : Observable<Array<ActionLogGSM>>{
    return this.http.get<Array<ActionLogGSM>>(this.url+"/getActions/"+userID, this.httpSecurity);
  }

}
