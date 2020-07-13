import {Component, OnInit, ViewChild} from '@angular/core';
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
import {MatStep} from "@angular/material/stepper";
import {MatDatepickerInputEvent} from "@angular/material/datepicker";

@Component({
  selector: 'app-export-info-dialog',
  templateUrl: './export-info-dialog.component.html',
  styleUrls: ['./export-info-dialog.component.css']
})
export class ExportInfoDialogComponent implements OnInit {
  user: User;
  locations: Array<LocationModel>;
  gsms: Dictionary<Dictionary<Array<GSMController>>> = {};
  rooms: Dictionary<Array<Room>> = {};
  methods: string[] = ['Mail', 'PDF', 'Word'];
  @ViewChild("step1") step1: MatStep;
  @ViewChild("step2") step2: MatStep;
  @ViewChild("step3") step3: MatStep;
  @ViewChild("step4") step4: MatStep;
  @ViewChild("step5") step5: MatStep;

  constructor(public dialogRef:MatDialogRef<ExportInfoDialogComponent>, private userService: UserService, private locationService: LocationService,
              private roomService: RoomService, private gsmService: GsmControllerService) {
    this.allTheTime=false;
    this.userService.getUserByCredential(localStorage.getItem("user")).subscribe(user => {
      this.user = user;
      this.locationService.getLocations(user.id).subscribe(locations => {
        this.locations = locations;
      })
    })}

  ngOnInit(): void {
  }

  getRoom(selected: MatListOption[]) {
    //this.rooms = new Array<RoomDTO>();
    this.rooms = {};
    selected.forEach(location =>{
      this.roomService.getRooms(this.user.id, location.value.id).subscribe(rooms =>{
        this.rooms[location.value.name] = rooms;
      })
    });
    this.step1.completed = selected.length > 0;
  }

  getGsm(locationName: string, selected: MatListOption[]){
   this.gsms[locationName] = {};
    selected.forEach(room=>{
      this.gsmService.getGSMs(this.user.id, room.value.id).subscribe(controller=>{
        this.gsms[locationName][room.value.name] = controller;
      });
    });
    if(selected.length == 0){
      delete this.gsms[locationName];
    }
    this.step2.completed = selected.length > 0;
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

  changeGSM(selected: MatListOption[]) {
    this.step3.completed = selected.length > 0;
  }

  isStep4Completed(val:boolean){
    if(val){
      this.allTheTime=!this.allTheTime;
    }
    this.step4.completed= this.allTheTime==true || (this.dateStart!=undefined && this.dateFinish!=undefined);
  }

  dateStart;
  dateFinish;
  allTheTime: boolean;
  changeFinishDate($event: MatDatepickerInputEvent<unknown>) {
    console.log($event.value);
    this.dateFinish=$event.value;
    this.isStep4Completed(false);
  }

  changeStartDate($event: MatDatepickerInputEvent<unknown>) {
    console.log($event.value);
    this.dateStart=$event.value;
    this.isStep4Completed(false);
  }
}


export class RoomDTO {
  locationName: string;
  rooms: Room[];
  constructor(private name: string, private roomList: Room[]) {
    this.locationName = name;
    this.rooms = roomList;
  }
}

export class GSMDTO{
  roomName: string;
  gsms: GSMController[];
  constructor(private namer: string, private gsmList: GSMController[]) {
    this.roomName = namer;
    this.gsms = gsmList;
  }
}

export interface Dictionary<T> {
  [K: string]: T;
}
