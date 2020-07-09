import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../service/userService";
import {User} from "../../model/user";
import {MatSnackBar} from "@angular/material/snack-bar";
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
import {LocationDialogComponent} from "../location-dialog/location-dialog.component";

export interface DialogData {
oldPassword: string;
newPassword: string;}


  export interface LoginDialogData
{
  type: string;
  name: string;
  latitude: string;
  longitude: string;
  city: string;
  //TODO: delete change-password-dialog+ those 2 fields bellow
   oldPassword: string;
   newPassword: string;
   gsm_type: string;
   phoneNumber:string;
   status: string;
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
  rooms: Room[];

  expandedController: GSMController | null;
  controllerColumns: string[] = ["type", "status", "phoneNumber"];
  controllerDataSource;
  gsmController: Array<GSMController>;
  room: Room;

  applyFilter(event: Event, dataSource) {
    const filterValue = (event.target as HTMLInputElement).value;
    dataSource.filter = filterValue.trim().toLowerCase();
  }

  constructor(
    public dialog: MatDialog,
    private router: Router,
    private userService: UserService,
    private locationService: LocationService,
    private roomService: RoomService,
    private gsmControllerService: GsmControllerService,
    public snackBar: MatSnackBar) {
    if (localStorage.getItem("user") == "null") {
      this.router.navigate(["/unauthorizedaccess"]);
    }
  }

  AddGSM() {
        const dialogRef=this.dialog.open(AddGSMComponent,{
        width: '300px',
        data:{type:"Add"}
      });
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        console.log(result);
        //result can be null on cancel
        if(result!=null) {
          //TODO: validation for latitude/longitude + refresh table
          var gsmController=new GSMController(-1,-1,result.phoneNumber,result.status,result.gsm_type);
          this.gsmControllerService.addGSMController(this.user.id,gsmController).subscribe(response=>{
            this.snackBar.open(String("Added GSM."),"Ok",{duration:2000});
            this.gsmControllerService.getGSMs(this.user.id, this.room.id).subscribe(GSMController =>{
              this.gsmController = GSMController;
              this.controllerDataSource = new MatTableDataSource<GSMController>(GSMController);
            });
          });
        }
      });
    }

  UpdateGSM(controller: any){
    const dialogRef=this.dialog.open(UpdateGSMComponent,{
      width: '300px',
      data:{type:"Update",
        gsm_type: controller["gsm_type"],
        status: controller["status"],
        phoneNumber: controller["phoneNumber"]
      }
    });
    dialogRef.afterClosed().subscribe(result=>{
      console.log('The dialog was closed');
      console.log(result);
      if(result!=null){
        var gsmcontroller = new GSMController(-1,-1,result.phoneNumber,result.status,result.gsm_type);
        this.gsmControllerService.updateGSMController(this.user.id,gsmcontroller).subscribe(response =>{
          this.snackBar.open(String("Updated GSM"),"ok",{duration:2000});
          this.gsmControllerService.getGSMs(this.user.id, this.room.id).subscribe(GSMController =>{
          this.gsmController=GSMController;
          this.controllerDataSource= new MatTableDataSource<GSMController>(GSMController);
        });
      });
    }
  });
  }

  DeleteGSM(controller:any){
    const dialogRef=this.dialog.open(DeleteGSMComponent);
   /* const dialogRef=this.dialog.open(DeleteGSMComponent,{
      width: '300px',
      data:{type:"Delete",
        gsm_type:controller["gsm_type"],
        status: controller["status"],
        phoneNumber: controller["phoneNumber"]}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      if(result!=null) {
        this.snackBar.open(String("Deleted GSM."),"Ok",{duration:2000});
      }
    });*/
  }

  setProfile(){
    this.page="Profile";
  }

  setManageLocations() {
    this.page = "ManageLocations"
  }

  setManageRooms() {
    this.page = "ManageRooms"
  }

  setManageController() {
    this.page = "ManageController"
  }

  setSecurity() {
    this.page = "Security"
  }

  ngOnInit(): void {
    this.page = "Profile";
    var aux = localStorage.getItem("user");
    this.userService.getUserByCredential(aux).subscribe(user => {
      this.user = user;
      this.locationService.getLocations(this.user.id).subscribe(locations => {
        this.locations = locations;
        this.locationDataSource = new MatTableDataSource<LocationModel>(locations);
      });
    });
  }

  logOut() {
    localStorage.setItem("user", null);
    this.router.navigate(["/home"]);
  }

  checkPassword(password: string): boolean {
    //just to play around
    var rez = true;
    if (password.length < 7) {
      rez = false;
    }
    return rez;

  }

  isProfile() {
    return this.page === "Profile";
  }

  isSecurity() {
    return this.page === "Security";
  }

  isManageLocations() {
    return this.page === "ManageLocations";
  }

  changePassword2(oldPassword: string, newPassword: string, confirmPassword: string) {
    if (this.user.password === oldPassword) {
      if (newPassword === confirmPassword) {
        if (!this.checkPassword(newPassword)) {
          this.snackBar.open("The new password is too short.", "Ok", {duration: 2000});
        } else {
          this.user.password = newPassword;
          console.log("changing password ", newPassword);
          this.userService.changePassword(this.user.id, this.user).subscribe(response => {
            console.log(response);
            this.snackBar.open("Password changed.", "Ok", {duration: 2000});
          });
        }
      } else {
        this.snackBar.open("The new password and confirm password are different.", "Ok", {duration: 2000});
      }
    } else {
      this.snackBar.open("The old password is incorect.", "Ok", {duration: 2000});
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
    dialogRef.afterClosed().subscribe(name => {
      console.log(name);
        if (name != null) { //fix this
          var room = new Room(-1, this.expandedLocation.id, name);
          this.roomService.addRoom(this.user.id, room).subscribe(response => {
            this.snackBar.open(String(" The room has been added"), "OK", {duration: 2000})
            this.roomService.getRooms(this.user.id, this.expandedLocation.id).subscribe(rooms => {
              this.rooms=rooms;
              this.roomDataSource=new MatTableDataSource<Room>(rooms);
            })
          });
        }
      })
  }

  deleteRoom() {
    const dialogRef = this.dialog.open(DeleteRoomComponent)
    dialogRef.afterClosed().subscribe(result => {
        if (result == true) { //fix this
        /*  var room = new Room(this.expandedRoom.id,this.expandedLocation.id, this.expandedRoom.name);
          this.roomService.deleteRoom(this.user.id, this.expandedRoom.id).subscribe(response =>{
          this.snackBar.open(String(" The room has been deleted"), "OK", {duration: 2000})
          this.roomService.getRooms(this.user.id, this.expandedLocation.id).subscribe(rooms => {
            this.rooms = rooms;
            this.roomDataSource = new MatTableDataSource<Room>(rooms);
          })
        }) */
        }
      }
    )
  }

  updateRoom() {
    const dialogRef = this.dialog.open(UpdateRoomComponent)
    dialogRef.afterClosed().subscribe(name => {
        if (name != null) { //fix this
          var room = new Room(this.expandedRoom.id,this.expandedLocation.id, name);
          this.roomService.updateRoom(this.user.id, room).subscribe(response => {
            this.snackBar.open(String(" The room has been updated"), "OK",{duration:2000})
            this.roomService.getRooms(this.user.id, this.expandedLocation.id).subscribe(rooms => {
              this.rooms=rooms;
              this.roomDataSource=new MatTableDataSource<Room>(rooms);
            })
          });
        }
        })
      }



    isPastActions()
    {
      return this.page === "PastActions"
    }

    setPastAction()
    {
      this.page = "PastActions"
    }


    addLocation(){
      const dialogRef = this.dialog.open(LocationDialogComponent, {
        width: '300px',
        data: {type: "Add"}
      });
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        console.log(result);
        //result can be null on cancel
        if (result != null) {
          //TODO: validation for latitude/longitude + refresh table
          var locationModel = new LocationModel(-1, result.latitude, result.longitude, "", result.name, result.city, this.user.id);
          this.locationService.addLocation(this.user.id, locationModel).subscribe(response => {
            this.snackBar.open(String("Added location."), "Ok", {duration: 2000});
            this.locationService.getLocations(this.user.id).subscribe(locations => {
              this.locations = locations;
              this.locationDataSource = new MatTableDataSource<LocationModel>(locations);
            });
          });
        }
      });
    }

    updateLocation(location:any){
      const dialogRef = this.dialog.open(LocationDialogComponent, {
        width: '300px',
        data: {
          type: "Update",
          name: location["name"],
          latitude: location["latitude"],
          longitude: location["longitude"],
          city: location["city"]
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        console.log(result);
        //result can be null on cancel
        if (result != null) {
          var locationModel = new LocationModel(-1, result.latitude, result.longitude, "", result.name, result.city, this.user.id);
          this.snackBar.open(String("Updated location."), "Ok", {duration: 2000});
          /*
          this.locationService.updateLocation(this.user.id,locationModel).subscribe(response=>{
            this.snackBar.open(String("Added location."),"Ok",{duration:2000});
          });
          */
        }
      });
    }

    deleteLocation(location:any){
      const dialogRef = this.dialog.open(LocationDialogComponent, {
        width: '300px',
        data: {
          type: "Delete",
          name: location["name"]
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        console.log(result);
        //result can be null on cancel
        if (result != null) {
          this.snackBar.open(String("Deleted location."), "Ok", {duration: 2000});
        }
      });
    }
  }

