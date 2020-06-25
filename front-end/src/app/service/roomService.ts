import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../model/Room";

@Injectable({providedIn: 'root'})
export class RoomService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  private url = 'http://localhost:8080/api/room';

  constructor(private http: HttpClient) {
  }

  addRoom(userID : number, room : Room) : Observable<Response>{
    // here you have to be careful of the 400 error : t means that there
    // some validation errors in the server
    return this.http.put<Response>(this.url+"/addRoom/"+userID,room,this.httpOptions);
  }

  updateRoom(userID : number, room : Room) : Observable<Response>{
    // here you have to be careful of the 400 error : t means that there
    // some validation errors in the server
    return this.http.put<Response>(this.url+"/updateRoom/"+userID,room,this.httpOptions);
  }

  getRooms(userID : number, locationID : number) : Observable<Array<Room>>{
    return this.http.get<Array<Room>>(this.url+"/getRooms/"+userID+"/"+locationID,this.httpOptions);
  }
}
