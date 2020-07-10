import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatSlideToggle, MatSlideToggleChange} from "@angular/material/slide-toggle";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {DeleteButtonDialogComponent} from "../delete-button-dialog/delete-button-dialog.component";
import {Router} from "@angular/router";
import {GSMController} from "../../model/GSMController";
import {Room} from "../../model/Room";
import {LocationService} from "../../service/locationService";
import {RoomService} from "../../service/roomService";
import {GsmControllerService} from "../../service/gsmControllerService";
import {User} from "../../model/user";
import {UserService} from "../../service/userService";
import {LocationModel} from "../../model/LocationModel";
import {MatListOption, MatSelectionList} from "@angular/material/list";
import {Observable} from "rxjs";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  CurrentDate = new Date();
  WeatherData: any;
  locations: Array<LocationModel>;
  gsms: Array<GSMController>;
  currentLocation: LocationModel;
  rooms: Array<Room>;
  currentRoom: Room;
  window: GSMController;
  door: GSMController;
  user: User;
  locationList: string[];
  roomList: string[];
  image: string;
  @ViewChild("door") refDoor: MatSlideToggle;
  @ViewChild("window") refWindow: MatSlideToggle;
  toggleDoor: MatSlideToggle;
  toggleWindow: MatSlideToggle;
  roomLength: number;

  usersObservable: Observable<User>;
  locationsObservable: Observable<LocationModel[]>;
  roomsObservable: Observable<Room[]>;
  controllersObservable: Observable<GSMController[]>;

  locationsLoaded: Promise<boolean>;
  roomsLoaded: Promise<boolean>;
  gsmsLoaded: Promise<boolean>;
  startLoading: Promise<boolean>;

  imageExists: boolean = true;

  constructor(public snackBar: MatSnackBar, private router: Router, private dialog: MatDialog, private locationService: LocationService,
              private roomService: RoomService, private gsmService: GsmControllerService,
              private userService: UserService) {
    setInterval(() => {
      this.CurrentDate = new Date();
    }, 1);
    this.locationsLoaded = new Promise<boolean>( resolve=>{
      this.usersObservable = this.userService.getUserByCredential(localStorage.getItem("user"));
      this.userService.getUserByCredential(localStorage.getItem("user")).subscribe(user => {
        this.user = user;
        // resolve();
        this.locationService.getLocations(user.id).subscribe(locations => {
          resolve();
          this.locations = locations;
          console.log(`got location`);
        })
      })
    });
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
      verticalPosition: 'bottom',
      horizontalPosition: 'left'

    });
  }

  ngOnInit(): void {
    this.WeatherData = {
      main: {},
      isDay: true
    };
    this.locationsLoaded.then(()=>{
      this.locationsObservable = this.locationService.getLocations(this.user.id);
    });
  }

  getLocationName() {
    if(this.currentLocation) {
      return this.currentLocation.name;
    }
  }

  getRoomName() {
    if(this.currentRoom) {
      return this.currentRoom.name;
    }
  }

  getImage(){
    return this.image;
  }

  setImage() {
      if(this.gsms.length>0) {
        if (this.door.status == "ON" && this.window.status == "ON") {
          this.image = `assets/${this.currentLocation.name}/${this.currentRoom.name}/openHouse.png`
        } else if (this.door.status == "ON" && this.window.status == "OFF") {
          this.image = `assets/${this.currentLocation.name}/${this.currentRoom.name}/openDoor.png`
        } else if (this.door.status == "OFF" && this.window.status == "ON") {
          this.image = `assets/${this.currentLocation.name}/${this.currentRoom.name}/openWindow.png`
        } else if (this.door.status == "OFF" && this.window.status == "OFF"){
          this.image = `assets/${this.currentLocation.name}/${this.currentRoom.name}/closedHouse.png`
        }
        else {
          this.image = null;
        }
      }
    if(this.image!==null){
      this.imageExists = true;
    }
  }

  doorChange($event: MatSlideToggleChange) {
    if ($event.checked) {
      this.gsmService.openGSM(this.door, this.user.id).subscribe(response => {
        if (response) {
          this.openSnackBar("Opened door", "OK");
          this.door.status = "ON";
          this.setImage()
        } else {
          this.refDoor.toggle();
          this.openSnackBar("Something went wrong", "OK");
        }

      });
    } else {
      this.gsmService.closeGSM(this.door, this.user.id).subscribe(response => {
        if (response) {
          this.openSnackBar("Closed door", "OK");
          this.door.status = "OFF";
          this.setImage()
        } else {
          this.refDoor.toggle();
          this.openSnackBar("Something went wrong", "OK");
        }
      });
    }
  }

  windowChange($event: MatSlideToggleChange) {
    if ($event.checked) {
      this.gsmService.openGSM(this.window, this.user.id).subscribe(response => {
        if (response) {
          this.openSnackBar("Opened window", "OK");
          this.window.status = "ON";
          this.setImage()
        } else {
          this.refWindow.toggle();
          this.openSnackBar("Something went wrong", "OK");
        }
      });
    } else {
      this.gsmService.closeGSM(this.window, this.user.id).subscribe(response => {
        if (response) {
          this.openSnackBar("Closed window", "OK");
          this.window.status = "OFF";
          this.setImage()
        } else {
          this.refWindow.toggle();
          this.openSnackBar("Something went wrong", "OK");
        }
      });
    }
  }

  getWeatherData() {
    const formatUrl = (lat, long) => `http://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=2ab187c4fc0fb4ea8bb6308cfb4d2324`;
    const url = formatUrl(this.currentLocation.latitude, this.currentLocation.longitude);
    fetch(url)
      .then(response => response.json())
      .then(data => {this.setWeatherData(data);});
  }

  setWeatherData(data) {
    this.WeatherData = data;
    let sunsetTime = new Date(this.WeatherData.sys.sunset * 1000);
    let sunriseTime = new Date(this.WeatherData.sys.sunrise * 1000);
    this.WeatherData.sunset_time = sunsetTime.toLocaleTimeString();
    let currentDate = new Date();
    this.WeatherData.isDay = (currentDate.getTime() < sunsetTime.getTime() && currentDate.getTime() > sunriseTime.getTime());
    this.WeatherData.temp_celsius = (this.WeatherData.main.temp - 273.15).toFixed(0);
    this.WeatherData.temp_min = (this.WeatherData.main.temp_min - 273.15).toFixed(0);
    this.WeatherData.temp_max = (this.WeatherData.main.temp_max - 273.15).toFixed(0);
    this.WeatherData.temp_feels_like = (this.WeatherData.main.feels_like - 273.15).toFixed(0);
  }

  isUserLoggedIn() {
    if (localStorage.getItem("user") == "null") {
      this.router.navigate(["/unauthorizedaccess"]);
      return false;
    }
    return true;
  }

  deleteButton() {
    const dialogRef = this.dialog.open(DeleteButtonDialogComponent)
    dialogRef.afterClosed().subscribe(result => {
        if (result == true) { //fix this
          this.openSnackBar("The controller was deleted", "OK");
        }
      }
    )
  }

  setStatusToggles(){
    this.locationsLoaded.then(() =>{
      if (this.door.status == "ON" && this.refDoor.checked==false) {
        this.refDoor.toggle();
      }
      if (this.window.status == "ON" && this.refWindow.checked==false) {
        this.refWindow.toggle();
      }
      if (this.door.status == "OFF" && this.refDoor.checked) {
        this.refDoor.toggle();
      }
      if (this.window.status == "OFF" && this.refWindow.checked) {
        this.refWindow.toggle();
      }
    })
      .catch(error => console.log(error));
  }
  changeRoom(selected: MatListOption[]) {
    this.locationsLoaded.then(()=>{
      this.roomsLoaded = new Promise<boolean>(resolve => {
        this.gsmsLoaded = new Promise<boolean>(resolve1 => {
          this.locations.forEach(location => {
            if (location.name == selected[0].value) {
              this.currentLocation = location;
              this.getWeatherData();
              this.roomsObservable = this.roomService.getRooms(this.user.id, this.currentLocation.id);
              this.roomService.getRooms(this.user.id, this.currentLocation.id).subscribe(rooms => {
                this.rooms = rooms;
                this.roomLength=rooms.length;
                if(this.rooms.length!=0) {
                  resolve(true);
                  this.currentRoom = null;
                }
                else {
                  resolve(false);
                  this.currentRoom = new Room(0, 0, "Sorry. There are no rooms here.");
                }
                this.gsms = [];
                this.setImage();
                resolve1();
              });
            }
          });
        })
      })
    })
  }

  changeControllers(selected: MatListOption[]) {
    this.roomsLoaded.then(()=>{
      this.gsmsLoaded = new Promise<boolean>(resolve => {
        this.startLoading = new Promise<boolean>(resolve1 => {
          this.rooms.forEach(room => {
            if (room.name == selected[0].value) {
              this.currentRoom = room;
              resolve1(true);
              this.controllersObservable = this.gsmService.getGSMs(this.user.id, this.currentRoom.id);
              this.gsmService.getGSMs(this.user.id, this.currentRoom.id).subscribe(gsms => {
                this.gsms=gsms;
                if(gsms.length==0) {
                  this.openSnackBar(this.currentRoom.name + " has no controllers", "Ok");
                  resolve(false);
                }
                //fix this later
                if(gsms.length==2) {
                  resolve(true);
                  if (gsms[0].type == "door") {
                    this.door = gsms[0];
                    this.window = gsms[1];
                  } else {
                    this.door = gsms[1];
                    this.window = gsms[0];
                  }
                  this.setImage();
                  this.setStatusToggles()
                }
              })
            }
          });
        })
      })
    })
  }

  controllerToggled(controller : GSMController) {
    // if(controller.type==="door"){
    //   this.door = controller;
    // }
    // else{
    //   this.window = controller;
    // }
  }
}
