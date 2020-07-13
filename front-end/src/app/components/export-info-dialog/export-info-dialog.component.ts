import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {UserService} from "../../service/userService";
import {LocationService} from "../../service/locationService";
import {User} from "../../model/user";
import {LocationModel} from "../../model/LocationModel";
import {MatListOption} from "@angular/material/list";
import {Room} from "../../model/Room";
import {RoomService} from "../../service/roomService";
import {GSMController} from "../../model/GSMController";
import {GsmControllerService} from "../../service/gsmControllerService";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-export-info-dialog',
  templateUrl: './export-info-dialog.component.html',
  styleUrls: ['./export-info-dialog.component.css']
})
export class ExportInfoDialogComponent implements OnInit {
  user: User;
  locations: Array<LocationModel>;
  currentLocation: LocationModel;
  currentRoom: Room;
  gsms: Array<GSMController>;
  rooms: Array<Room>;
  totalRooms: Array<Room>;
  methods: string[] = ['Mail', 'PDF', 'Word'];

  constructor(public dialogRef:MatDialogRef<ExportInfoDialogComponent>, private userService: UserService, private locationService: LocationService,
              private roomService: RoomService, private gsmService: GsmControllerService) {
    this.userService.getUserByCredential(localStorage.getItem("user")).subscribe(user => {
      this.user = user;
      this.locationService.getLocations(user.id).subscribe(locations => {
        this.locations = locations;
      })
    })}

  ngOnInit(): void {
  }

  getRoom(selected: MatListOption[]) {
    this.locations.forEach(location => {
      selected.forEach(selected =>{
        if (location.name == selected.value) {
          this.currentLocation = location;
          this.roomService.getRooms(this.user.id, this.currentLocation.id).subscribe(rooms => {
            this.rooms = rooms;
          });
        }}
      )
      this.totalRooms=this.rooms;
    });
  }
  getGsm(selected: MatListOption[]){
    this.rooms.forEach(room => {
      selected.forEach(selected =>{
        if (room.name == selected.value) {
          this.currentRoom =room;
          this.gsmService.getGSMs(this.user.id, this.currentRoom.id).subscribe(gsms => {
            this.gsms = gsms;
          });
        }}
      )
    });

  }

  closeDialog(){
    this.dialogRef.close(false);
  }

  export(selected: MatListOption[]){
    //by default send to email
    //this.userService.sendRaportViaEmail(this.user.id,starDate,endDate,takeAll).subsribe(respones=>{
    //console.log("raport sent")
    //})
  }

}
