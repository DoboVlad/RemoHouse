import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../model/Room";

@Injectable({providedIn: 'root'})
export class RoomService {
  private httpOptionsPlain = {
    headers: new HttpHeaders({
      'Accept': 'text/plain',
      'Content-Type': 'text/plain'
    }),
    responseType: 'text' as 'json'
  };

  private url = 'http://localhost:8080/api/room';

  constructor(private http: HttpClient) {
  }

  addRoom(userID : number, room : Room) : Observable<Response>{
    // here you have to be careful of the 400 error : t means that there
    // some validation errors in the server
    return this.http.put<Response>(this.url+"/addRoom/"+userID,room);
  }

  updateRoom(userID : number, room : Room) : Observable<Response>{
    // here you have to be careful of the 400 error : t means that there
    // some validation errors in the server
    return this.http.put<Response>(this.url+"/updateRoom/"+userID,room);
  }


  deleteRoom(userID : number, roomID : number) : Observable<string>{
    return this.http.delete<string>(this.url+"/deleteRoom/"+userID+"/"+roomID,this.httpOptionsPlain);
  }

  getRooms(userID : number, locationID : number) : Observable<Array<Room>>{
    return this.http.get<Array<Room>>(this.url+"/getRooms/"+userID+"/"+locationID);
  }
}
