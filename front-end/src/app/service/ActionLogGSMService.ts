import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {ActionLogGSM} from "../model/ActionLogGSM";

@Injectable({providedIn: 'root'})
export class ActionLogGSMService {

  private url = 'http://localhost:8080/api/actionLogGSM';

  constructor(private http: HttpClient) {
  }

  getActions(userID : number) : Observable<Array<ActionLogGSM>>{
    return this.http.get<Array<ActionLogGSM>>(this.url+"/getActions/"+userID);
  }

}
