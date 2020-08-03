import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../model/user";
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class UserService {
  // httpOptions = {
  //   headers: new HttpHeaders({'Content-Type': 'application/json'})
  // };

  private url = 'http://localhost:8080/api/user';
  private httpOptionsPlain = {
    headers: new HttpHeaders({
      'Accept': 'text/plain',
      'Content-Type': 'text/plain',
      Authorization : `Basic ${window.btoa('user:password')}`
    }),
    responseType: 'text' as 'json'
  };
  private httpOptions = {
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
  constructor(private http: HttpClient) {
  }

  login(user : User) : Observable<Response>{
    return this.http.put<Response>(this.url+'/login', user, this.httpSecurity);
  }

  signup(user : User) : Observable<Response>{
    // here you have to be careful of the 400 error : t means that there
    // some validation errors in the server
    return this.http.post<Response>(this.url+"/signUp",user,this.httpSecurity);
  }

  changePassword(userID : number, user : User) : Observable<Response>{
    // here you have to be careful of the 400 error : t means that there
    // some validation errors in the server
    return this.http.put<Response>(this.url+"/changePassword/"+userID,user,this.httpSecurity);
  }

  getUserByCredential(credential : string) : Observable<User>{
    return this.http.get<User>(this.url+"/getUserByCredential/"+credential,this.httpSecurity);
  }

  sendCode(credential:string) : Observable<any>{
    return this.http.get<any>(this.url+"/recoverPassword/"+credential,this.httpOptionsPlain);
  }

  sendConfirmationCode(email:string):Observable<string>{
    return this.http.get<string>(this.url+"/confirmEmail/"+email,this.httpOptionsPlain);
  }

  validateAccount(email : string){
    return this.http.get(this.url+"/validateAccount/"+email,this.httpSecurity)
  }

  sendRaportViaEmail(gsms:Array<number>, userID : number, startDate:string, endDate:string, takeAll:boolean) : Observable<string>{
    return this.http.put<string>(this.url+"/sendEmailActionsFromGSMs/"+userID+"/pdf/"+startDate+"/"+endDate+"/"+takeAll,gsms,this.httpOptions)
  }

  verifyOldPassword(password:string,user:number):Observable<boolean>{
    return this.http.put<boolean>(this.url+"/checkPassword/"+user,password,this.httpSecurity)
  }
}
