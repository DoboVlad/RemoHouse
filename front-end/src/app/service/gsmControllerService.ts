import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {GSMController} from "../model/GSMController";

@Injectable({providedIn: 'root'})
export class GsmControllerService {
  private httpOptionsPlain = {
    headers: new HttpHeaders({
      'Accept': 'application/json',
      'Content-Type': 'application/json',
      Authorization : `Basic ${window.btoa('user:password')}`
    }),
    responseType: 'text' as 'json'
  };
  private httpSecurity = {
    headers: new HttpHeaders({
      Authorization : `Basic ${window.btoa('user:password')}`
    })
  };
  private url = 'http://localhost:8080/api/gsm';

  constructor(private http: HttpClient) {
  }

  openGSM(gsm : GSMController, userID : number) : Observable<Boolean>{
    return this.http.put<Boolean>(this.url+"/open/"+userID+"/open",gsm,this.httpSecurity);
  }
  closeGSM(gsm : GSMController, userID : number) : Observable<Boolean>{
    return this.http.put<Boolean>(this.url+"/close/"+userID+"/close",gsm,this.httpSecurity);
  }
  getGSMs(userID : number, roomID : number) : Observable<Array<GSMController>>{
    return this.http.get<Array<GSMController>>(this.url+"/getGSMs/"+userID+"/"+roomID,this.httpSecurity);
  }

  addGSMController(userID : number, gsm : GSMController) : Observable<string>{
    return this.http.put<string>(this.url+"/addGSM/"+userID,gsm,this.httpOptionsPlain);
  }

  updateGSMController(userID : number, gsm : GSMController) : Observable<boolean>{
    return this.http.put<boolean>(this.url+"/update/"+userID,gsm,this.httpOptionsPlain);
  }

  deleteGSM(userID : number, gsmID : number) : Observable<boolean>{
    return this.http.delete<boolean>(this.url+"/delete/"+userID+"/"+gsmID,this.httpOptionsPlain);
  }

}
