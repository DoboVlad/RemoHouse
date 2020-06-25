import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Room} from "../model/Room";
import {LocationModel} from "../model/LocationModel";

@Injectable({providedIn: 'root'})
export class LocationService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  private url = 'http://localhost:8080/api/location';

  constructor(private http: HttpClient) {
  }

  addLocation(userID : number, location : LocationModel) : Observable<Response>{
    // here you have to be careful of the 400 error : t means that there
    // some validation errors in the server
    return this.http.post<Response>(this.url+"/addLocation/"+userID,location,this.httpOptions);
  }

  getLocations(userID : number) : Observable<Array<LocationModel>>{
    // here you have to be careful of the 400 error : t means that there
    // some validation errors in the server
    return this.http.get<Array<LocationModel>>(this.url+"/getLocations/"+userID,this.httpOptions);
  }

}
