import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {GSMController} from "../model/GSMController";

@Injectable({providedIn: 'root'})
export class GsmControllerService {

  private url = 'http://localhost:8080/api/gsm';

  constructor(private http: HttpClient) {
  }

  openGSM(gsm : GSMController, userID : number) : Observable<Response>{
    return this.http.put<Response>(this.url+"/open/"+userID+"/open",gsm);
  }
  closeGSM(gsm : GSMController, userID : number) : Observable<Response>{
    return this.http.put<Response>(this.url+"/close/"+userID+"/close",gsm);
  }
  getGSMs(userID : number, roomID : number) : Observable<Array<GSMController>>{
    return this.http.get<Array<GSMController>>(this.url+"/getGSMs/"+userID+"/"+roomID);
  }
}
