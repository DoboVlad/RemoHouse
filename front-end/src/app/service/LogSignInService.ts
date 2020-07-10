import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {LogSignIn} from "../model/LogSignIn";

@Injectable({providedIn: 'root'})
export class LogSignInService {

  private url = 'http://localhost:8080/api/logSignIn';

  constructor(private http: HttpClient) {
  }

  addLog(logSignIn:LogSignIn) : boolean{
    return true;
  }
}
