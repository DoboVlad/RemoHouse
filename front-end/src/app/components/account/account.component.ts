import {AfterViewInit, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {UserService} from "../../service/userService";
import {User} from "../../model/user";
import {MatSnackBar} from "@angular/material/snack-bar";
import {LocationService} from "../../service/locationService";
import {LocationModel} from "../../model/LocationModel";
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
import {ActionLogGSM} from "../../model/ActionLogGSM";
import {ActionLogGSMService} from "../../service/ActionLogGSMService";
import {MatSort} from "@angular/material/sort";
import {MatPaginator} from "@angular/material/paginator";
import {MatTableDataSource} from "@angular/material/table";
import {LogSignIn} from "../../model/LogSignIn";
import {LogSignInService} from "../../service/LogSignInService";

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
  gsmController: GSMController[];
  room: Room;

  //actions page
  actionLogs: ActionLogGSM[] = [];
  dataSourceActions: MatTableDataSource<ActionLogGSM>;
  displayedColumns: string[] = ["operationType", "dateTime", "gsmControllerType", "roomName", "locationName"];
  resultsActionLength = 0;
  isLoadingResults: boolean = true;
  @ViewChild(MatSort, {static: false}) sort: MatSort;
  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;

  //sessions table
  logSignIns: LogSignIn[] = [];
  dataSourceLogs: MatTableDataSource<LogSignIn>;
  displayedColumnsLogs: string[] = ["ip", "browser", "browserVersion", "device", "os","osVersion","dateTime"];
  resultLengthLogs = 0;
  @ViewChild(MatSort, {static: false}) sortLogs: MatSort;
  @ViewChild(MatPaginator, {static: false}) paginatorLogs: MatPaginator;
  @ViewChild(MatSort) set matSort(ms: MatSort) {
    this.sort = ms;
    this.setDataSourceAttributes();
  }

  setDataSourceAttributes() {
    this.dataSourceActions.paginator = this.paginator;
    this.dataSourceActions.sort = this.sort;
    this.dataSourceLogs.sort = this.sortLogs;
    this.dataSourceActions.paginator = this.paginatorLogs;
  }

  applyFilter(event: Event, dataSource) {
    const filterValue = (event.target as HTMLInputElement).value;
    dataSource.filter = filterValue.trim().toLowerCase();
  }

  constructor(private cdr : ChangeDetectorRef, private logSignInService : LogSignInService, public dialog: MatDialog, private router: Router, private userService: UserService, private locationService: LocationService, private roomService: RoomService, private gsmControllerService: GsmControllerService, public snackBar: MatSnackBar, private actionLogService: ActionLogGSMService) {
    if (localStorage.getItem("user") == "null") {
      this.router.navigate(["/unauthorizedaccess"]);
    }
    this.page = "Security";
    this.userService.getUserByCredential(localStorage.getItem("user")).subscribe(user => {
      this.user = user;
      this.locationService.getLocations(this.user.id).subscribe(locations => {
        this.locations = locations;
        this.locationDataSource = new MatTableDataSource<LocationModel>(locations);
        this.actionLogService.getActions(this.user.id).subscribe(actions => {
          this.actionLogs = actions;
          this.dataSourceActions = new MatTableDataSource<ActionLogGSM>(actions);
          this.resultsActionLength = actions.length;
          this.isLoadingResults = false;
          this.logSignInService.getLogs(this.user.id).subscribe(logs=>{
            console.log(logs);
            this.logSignIns = logs;
            this.dataSourceLogs = new MatTableDataSource<LogSignIn>(logs);
            this.cdr.detectChanges()
            this.resultLengthLogs = logs.length;
          })
        })
      });
    });
  }

  ngOnInit(): void {
  }
  logOut() {
    localStorage.clear();
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

  //functions to determine the page
  isProfile() {
    return this.page === "Profile";
  }

  isSecurity() {
    return this.page === "Security";
  }

  isManageLocations() {
    return this.page === "ManageLocations";
  }

  isPastActions() {
    return this.page === "PastActions"
  }

  setProfile() {
    this.page = "Profile";
  }

  setManageLocations() {
    this.page = "ManageLocations"
  }

  setSecurity() {
    this.page = "Security"
  }

  setPastAction() {
    this.page = "PastActions"
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

  //GSM crud function
  AddGSM() {
    const dialogRef = this.dialog.open(AddGSMComponent, {
      width: '300px',
      data: {type: "Add"}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      //result can be null on cancel
      if (result != null) {
        //TODO: validation for latitude/longitude + refresh table
        var gsmController = new GSMController(0, this.expandedRoom.id, result.phoneNumber, result.status, result.gsm_type);
        this.gsmControllerService.addGSMController(this.user.id, gsmController).subscribe(response => {
          console.log(response);
          this.snackBar.open(String("Added GSM."), "Ok", {duration: 2000});
          this.gsmControllerService.getGSMs(this.user.id, this.room.id).subscribe(GSMController => {
            this.gsmController = GSMController;
            this.controllerDataSource = new MatTableDataSource<GSMController>(GSMController);
            this.refreshTable();
          });
        });
      }
    });
  }

  UpdateGSM(controller: any) {
    const dialogRef = this.dialog.open(UpdateGSMComponent, {
      width: '300px',
      data: {
        type: "Update",
        gsm_type: controller["gsm_type"],
        status: controller["status"],
        phoneNumber: controller["phoneNumber"]
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(controller);
      if (result != null) {
        console.log("Am ajuns aici!");
        var gsmcontroller = new GSMController(controller['id'], controller['roomID'], result.phoneNumber, controller['status'], result.gsm_type);
        this.gsmControllerService.updateGSMController(this.user.id, gsmcontroller).subscribe(controller => {
          this.snackBar.open(String("Updated GSM"), "ok", {duration: 2000});
          this.refreshTablegsm();
        });
      }
    });
  }

  refreshTablegsm() {
    this.gsmControllerService.getGSMs(this.user.id, this.expandedRoom.id).subscribe(controller => {
      this.gsmController = controller;
      this.controllerDataSource = new MatTableDataSource<GSMController>(controller);
    });
  }

  DeleteGSM(controller: any) {
    const dialogRef = this.dialog.open(DeleteGSMComponent, {
      width: '300px',
      data: {
        type: "Delete",
        gsm_type: controller["gsm_type"],
        status: controller["status"],
        phoneNumber: controller["phoneNumber"]
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      console.log(result);
      if (result != null) {
        this.gsmControllerService.deleteGSM(this.user.id, controller["id"]).subscribe(result => {
          this.snackBar.open(String("Deleted GSM."), "Ok", {duration: 2000});
          this.refreshTablegsm();
        });
      }
    });
  }

  //CRUD room
  addRoom() {
    const dialogRef = this.dialog.open(AddRoomComponent)
    dialogRef.afterClosed().subscribe(name => {
      console.log(name);
      if (name != null) { //fix this
        var room = new Room(-1, this.expandedLocation.id, name);
        this.roomService.addRoom(this.user.id, room).subscribe(response => {
          console.log(response)
          this.snackBar.open(String(" The room has been added"), "OK", {duration: 2000})
          this.roomService.getRooms(this.user.id, this.expandedLocation.id).subscribe(rooms => {
            this.rooms = rooms;
            this.roomDataSource = new MatTableDataSource<Room>(rooms);
          })
        });
      }
    })
  }

  deleteRoom() {
    const dialogRef = this.dialog.open(DeleteRoomComponent)
    dialogRef.afterClosed().subscribe(result => {
        if (result == true) {
          console.log(this.expandedRoom)
          this.roomService.deleteRoom(this.user.id, this.expandedRoom.id).subscribe(response => {
            console.log(response);
            this.snackBar.open(String(" The room has been deleted"), "OK", {duration: 2000})
            this.roomService.getRooms(this.user.id, this.expandedLocation.id).subscribe(rooms => {
              this.rooms = rooms;
              this.roomDataSource = new MatTableDataSource<Room>(rooms);
            })
          })
        }
      }
    )
  }

  updateRoom() {
    const dialogRef = this.dialog.open(UpdateRoomComponent)
    dialogRef.afterClosed().subscribe(name => {
      if (name != null) { //fix this
        var room = new Room(this.expandedRoom.id, this.expandedLocation.id, name);
        this.roomService.updateRoom(this.user.id, room).subscribe(response => {
          this.snackBar.open(String(" The room has been updated"), "OK", {duration: 2000})
          this.roomService.getRooms(this.user.id, this.expandedLocation.id).subscribe(rooms => {
            this.rooms = rooms;
            this.roomDataSource = new MatTableDataSource<Room>(rooms);
          })
        });
      }
    })
  }

  //CRUD location
  addLocation() {
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
          this.refreshTable();
        });
      }
    });
  }

  updateLocation(location: any) {
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
        var locationModel = new LocationModel(location["id"], result.latitude, result.longitude, location["image"], result.name, result.city, this.user.id);
        this.locationService.updateLocation(this.user.id, locationModel).subscribe(response => {
          this.snackBar.open(String("Update location."), "Ok", {duration: 2000});
          this.refreshTable();
        });
      }
    });
  }

  deleteLocation(location: any) {
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

        this.locationService.deleteLocation(this.user.id, location["id"]).subscribe(response => {
          this.snackBar.open(String("Delete location."), "Ok", {duration: 2000});
          this.refreshTable();
        });
      }
    });
  }

  refreshTable() {
    this.locationService.getLocations(this.user.id).subscribe(locations => {
      this.locations = locations;
      this.locationDataSource = new MatTableDataSource<LocationModel>(locations);
    });
  }


  applyFilterActions($event: KeyboardEvent) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourceActions.filter = filterValue.trim().toLowerCase();
    if (this.dataSourceActions.paginator) {
      this.dataSourceActions.paginator.firstPage();
    }
  }

  //    sessions table
  applyFilterLogs($event: KeyboardEvent) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourceLogs.filter = filterValue.trim().toLowerCase();
  }
}

