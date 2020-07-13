<<<<<<< HEAD
import { Component, OnInit } from '@angular/core';
=======
import {Component, OnInit, ViewChild} from '@angular/core';
>>>>>>> 1c8feeb7ed9989c0e4ccb4ebdec6fbb1c46c38f9
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
<<<<<<< HEAD
=======
import {MatStep} from "@angular/material/stepper";
import {MatDatepickerInputEvent} from "@angular/material/datepicker";
import {DatePipe} from "@angular/common";
>>>>>>> 1c8feeb7ed9989c0e4ccb4ebdec6fbb1c46c38f9

@Component({
  selector: 'app-export-info-dialog',
  templateUrl: './export-info-dialog.component.html',
  styleUrls: ['./export-info-dialog.component.css']
})
export class ExportInfoDialogComponent implements OnInit {
  user: User;
  locations: Array<LocationModel>;
<<<<<<< HEAD
  currentLocation: LocationModel;
  currentRoom: Room;
  gsms: Array<GSMController>;
  rooms: Array<Room>;
  totalRooms: Array<Room>;
  methods: string[] = ['Mail', 'PDF', 'Word'];

  constructor(public dialogRef:MatDialogRef<ExportInfoDialogComponent>, private userService: UserService, private locationService: LocationService,
              private roomService: RoomService, private gsmService: GsmControllerService) {
=======
  gsms: Dictionary<Dictionary<Array<GSMController>>> = {};
  selectedGSMs : Dictionary<Array<number>> = {};
  rooms: Dictionary<Array<Room>> = {};
  methods: string[] = ['Mail', 'PDF', 'Word'];
  @ViewChild("step1") step1: MatStep;
  @ViewChild("step2") step2: MatStep;
  @ViewChild("step3") step3: MatStep;
  @ViewChild("step4") step4: MatStep;
  @ViewChild("step5") step5: MatStep;

  dateStart = Date.now().toString();
  dateFinish =Date.now().toString();
  allTheTime: boolean;

  constructor(private datePipe:DatePipe, public dialogRef:MatDialogRef<ExportInfoDialogComponent>, private userService: UserService, private locationService: LocationService,
              private roomService: RoomService, private gsmService: GsmControllerService) {
    this.allTheTime=false;
>>>>>>> 1c8feeb7ed9989c0e4ccb4ebdec6fbb1c46c38f9
    this.userService.getUserByCredential(localStorage.getItem("user")).subscribe(user => {
      this.user = user;
      this.locationService.getLocations(user.id).subscribe(locations => {
        this.locations = locations;
      })
    })}

  ngOnInit(): void {
  }

  getRoom(selected: MatListOption[]) {
<<<<<<< HEAD
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

=======
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
>>>>>>> 1c8feeb7ed9989c0e4ccb4ebdec6fbb1c46c38f9
  }

  closeDialog(){
    this.dialogRef.close(false);
  }

  export(selected: MatListOption[]){
    //by default send to email
<<<<<<< HEAD
    //this.userService.sendRaportViaEmail(this.user.id,starDate,endDate,takeAll).subsribe(respones=>{
    //console.log("raport sent")
    //})
  }

=======
    let list = this.flattenSelectedGSMS()
    this.userService.sendRaportViaEmail(list,this.user.id,this.datePipe.transform(this.dateStart,"yyyy-MM-dd HH:mm:ss"),this.datePipe.transform(this.dateFinish,"yyyy-MM-dd HH:mm:ss"),this.allTheTime).subscribe(respones=>{
      if(respones=="email sent")
        alert("The report was e-mailed to you successfully.")
    })
  }

  private flattenSelectedGSMS() : Array<number> {
    let list = [];
    Object.entries(this.selectedGSMs).forEach(
        ([key, value]) => {
          value.forEach(v=>list.push(v))
        });
    console.log(list)
    return list;
  }
  changeGSM(roomName:string,selected: MatListOption[]) {
    let selectedIDs = []
    selected.forEach(s=>selectedIDs.push(s.value))
    this.selectedGSMs[roomName]=selectedIDs;
    console.log(selectedIDs,this.selectedGSMs)
    this.step3.completed = selected.length > 0;
  }

  isStep4Completed(val:boolean){
    if(val){
      this.allTheTime=!this.allTheTime;
    }
    this.step4.completed= this.allTheTime==true || (this.dateStart!=undefined && this.dateFinish!=undefined);
  }

  changeFinishDate($event: MatDatepickerInputEvent<any>) {
    this.dateFinish=$event.value;
    this.isStep4Completed(false);
  }

  changeStartDate($event: MatDatepickerInputEvent<any>) {
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
>>>>>>> 1c8feeb7ed9989c0e4ccb4ebdec6fbb1c46c38f9
}
