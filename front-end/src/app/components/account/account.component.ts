import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../service/userService";
import {User} from "../../model/user";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ChangePasswordDialogComponent} from "../change-password-dialog/change-password-dialog.component";
import {DataSource} from "@angular/cdk/collections";
import {Observable, of} from "rxjs";
import {LocationService} from "../../service/locationService";
import {LocationModel} from "../../model/LocationModel";
import {MatTableDataSource} from "@angular/material/table";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Room} from "../../model/Room";
import {RoomService} from "../../service/roomService";
import {GSMController} from "../../model/GSMController";
import {GsmControllerService} from "../../service/gsmControllerService";
import {AddGSMComponent} from "../add-gsm/add-gsm.component";
import {UpdateGSMComponent} from "../update-gsm/update-gsm.component";
import {DeleteGSMComponent} from "../delete-gsm/delete-gsm.component";
import {AddRoomComponent} from "../add-room/add-room.component";
import {DeleteRoomComponent} from "../delete-room/delete-room.component";
import {UpdateRoomComponent} from "../update-room/update-room.component";

export interface DialogData {
   oldPassword: string;
   newPassword: string;
}
@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', visibility: 'hidden' })),
      state('expanded', style({ height: '*', visibility: 'visible' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class AccountComponent implements OnInit {

  user: User;
  page: string;
  locations: LocationModel[];

  expandedLocation: LocationModel | null;
  locationColumns: string[] = ["name", "latitude", "longitude", "city"];
  locationDataSource;

  expandedRoom: Room | null;
  roomColumns: string[] = ["name"];
  roomDataSource;

  expandedController: GSMController | null;
  controllerColumns: string[] = ["type", "status", "phoneNumber"];
  controllerDataSource;

  applyFilter(event: Event, dataSource) {
    const filterValue = (event.target as HTMLInputElement).value;
    dataSource.filter = filterValue.trim().toLowerCase();
  }

  constructor(
    public dialog:MatDialog,
    private router : Router,
    private userService : UserService,
    private locationService: LocationService,
    private roomService: RoomService,
    private gsmControllerService: GsmControllerService,
    public snackBar: MatSnackBar) {

  }
  AddGSM(){
    const dialogRef= this.dialog.open(AddGSMComponent);
  }
  UpdateGSM(){
    const dialogRef=this.dialog.open(UpdateGSMComponent);
  }
  DeleteGSM(){
    const dialogRef=this.dialog.open(DeleteGSMComponent);
  }
  setProfile(){
    this.page="Profile";
  }
  setManageLocations(){
    this.page="ManageLocations"
  }
  setManageRooms(){
    this.page="ManageRooms"
  }
  setManageController(){
    this.page="ManageController"
  }
  setSecurity() {
    this.page="Security"
  }

  ngOnInit(): void {
    this.page="Profile";
    var aux=localStorage.getItem("user");
    this.userService.getUserByCredential(aux).subscribe(user=>{
      this.user=user;
      this.locationService.getLocations(this.user.id).subscribe(locations =>{
        this.locations = locations;
        this.locationDataSource = new MatTableDataSource<LocationModel>(locations);
      });
    });
  }

  logOut() {
    localStorage.setItem("user",null);
    this.router.navigate(["/home"]);
  }

  checkPassword(password: string):boolean{
    //just to play around
    var rez=true;
    if(password.length<7){
      rez=false;
    }
    return rez;

  }

  changePassword() {
    const dialogRef=this.dialog.open(ChangePasswordDialogComponent,{
      data:{}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      //result can be null on cancel
      if(result!=null) {
        //check if old password is right
        if (result.oldPassword == this.user.password) {
          //check length password (just to be)
          if(!this.checkPassword(result.newPassword))
            this.snackBar.open("The new password is too short.","Ok",{duration:2000});
          else {
            this.user.password = result.newPassword;
            console.log("changing password ",result.newPassword);
            this.userService.changePassword(this.user.id, this.user).subscribe(response=>{
              console.log(response);
              this.snackBar.open("Password changed.","Ok",{duration:2000});
            });
          }
        } else
          this.snackBar.open("The old password is incorrect","Ok",{duration:2000});
      }
    });

  }

  isProfile() {
    return this.page==="Profile";
  }

  isSecurity() {
    return this.page==="Security";
  }

  isManageLocations(){
    return this.page==="ManageLocations";
  }

  changePassword2(oldPassword: string, newPassword: string, confirmPassword: string) {
    if(this.user.password===oldPassword){
      if(newPassword===confirmPassword) {
        if (!this.checkPassword(newPassword)) {
          this.snackBar.open("The new password is too short.","Ok",{duration:2000});
        }
        else{
          this.user.password = newPassword;
          console.log("changing password ",newPassword);
          this.userService.changePassword(this.user.id, this.user).subscribe(response=>{
            console.log(response);
            this.snackBar.open("Password changed.","Ok",{duration:2000});
          });
        }
      }
      else{
        this.snackBar.open("The new password and confirm password are different.","Ok",{duration:2000});
      }
    }
    else{
      this.snackBar.open("The old password is incorect.","Ok",{duration:2000});
    }
  }

  toggleLocationRow(location: LocationModel) {
    this.expandedLocation = this.expandedLocation === location ? null : location;
    this.roomService.getRooms(this.user.id, location.id).subscribe(rooms => {
      this.roomDataSource = new MatTableDataSource<Room>(rooms);
    });
  }

  toggleRooms(room: Room) {
    this.expandedRoom = this.expandedRoom === room ? null : room;
    this.gsmControllerService.getGSMs(this.user.id, room.id).subscribe(gsms => {
      this.controllerDataSource = new MatTableDataSource<GSMController>(gsms);
    })
  }
  addRoom() {
    const dialogRef = this.dialog.open(AddRoomComponent)
    dialogRef.afterClosed().subscribe(result => {
        if (result == true) { //fix this
        }
      }
    )
  }
  deleteRoom(){
    const dialogRef = this.dialog.open(DeleteRoomComponent)
    dialogRef.afterClosed().subscribe(result => {
        if (result == true) { //fix this
        }
      }
    )
  }
  updateRoom(){
    const dialogRef = this.dialog.open(UpdateRoomComponent)
    dialogRef.afterClosed().subscribe(result => {
        if (result == true) { //fix this
        }
      }
    )
  }
}
