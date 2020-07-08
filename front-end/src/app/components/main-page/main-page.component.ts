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

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit, AfterViewInit {

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
  @ViewChild("door") refDoor: MatSlideToggle;
  @ViewChild("window") refWindow: MatSlideToggle;
  roomLength: number;

  constructor(public snackBar: MatSnackBar, private router: Router, private dialog: MatDialog, private locationService: LocationService,
              private roomService: RoomService, private gsmService: GsmControllerService,
              private userService: UserService) {
    setInterval(() => {
      this.CurrentDate = new Date();
    }, 1);
    userService.getUserByCredential(localStorage.getItem("user")).subscribe(user => {
      this.user = user;
      locationService.getLocations(user.id).subscribe(locations => {
        this.locations = locations;
        locations.sort((n1,n2)=>{
          if (n1 < n2)
            return 1;
          if (n1 > n2)
            return -1;
          return 0;
        });
        this.currentLocation = locations[0];
        this.roomService.getRooms(this.user.id, this.currentLocation.id).subscribe(rooms => {
          this.rooms = rooms;
          if(rooms.length!=0) {
            this.currentRoom = rooms[0];
            this.gsmService.getGSMs(this.user.id, this.currentRoom.id).subscribe(gsms => {
              //fix this later
              if(gsms.length!=0) {
                this.gsms = gsms;
                if (gsms[0].type == "door") {
                  console.log("door");
                  this.door = gsms[0];
                  this.window = gsms[1];
                } else {
                  this.door = gsms[1];
                  this.window = gsms[0];
                }
              }
            })
          }
        })
      })
    })
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
    this.getWeatherData();
  }


  ngAfterViewInit(): void {
    if (this.door.status == "ON" && !this.refDoor.checked)
      this.refDoor.toggle();
    if (this.window.status == "ON" && !this.refWindow.checked)
      this.refWindow.toggle();
  }

  getLocationName() {
    return this.currentLocation.name;
  }

  getRoomName() {
    if(this.roomList ===[]){
      return this.currentRoom.name;
    }
    return this.roomList;
  }

  getImage() {
    if (this.rooms != [] && this.locations != [])
      if (this.door.status == "ON" && this.window.status == "ON") {
        return "assets/openHouse.png"
      } else if (this.door.status == "ON" && this.window.status == "OFF") {
        return "assets/openDoor.png"
      } else if (this.door.status == "OFF" && this.window.status == "ON") {
        return "assets/openWindow.png"
      } else {
        return "assets/closedHouse.png"
      }
  }

  doorChange($event: MatSlideToggleChange) {
    if ($event.checked) {
      this.gsmService.openGSM(this.door, this.user.id).subscribe(response => {
        if (response) {
          this.openSnackBar("Opened door", "OK");
          this.door.status = "ON";
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
        } else {
          this.refWindow.toggle();
          this.openSnackBar("Something went wrong", "OK");
        }
      });

    }
  }

  getWeatherData() {
    //API key 2ab187c4fc0fb4ea8bb6308cfb4d2324
    fetch('http://api.openweathermap.org/data/2.5/weather?lat=41.40338&lon=2.17403&appid=2ab187c4fc0fb4ea8bb6308cfb4d2324')
      .then(response => response.json())
      .then(data => {
        this.setWeatherData(data);
      });
    // let data = JSON.parse("{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"base\":\"stations\",\"main\":{\"temp\":287.329,\"pressure\":1012.69,\"humidity\":67,\"temp_min\":287.329,\"temp_max\":287.329,\"sea_level\":1020.15,\"grnd_level\":1012.69},\"wind\":{\"speed\":4.76,\"deg\":95.0004},\"clouds\":{\"all\":12},\"dt\":1476443177,\"sys\":{\"message\":0.004,\"country\":\"GB\",\"sunrise\":1476426249,\"sunset\":1476464855},\"id\":2643743,\"name\":\"London\",\"cod\":200}");
    // this.setWeatherData(data);
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
          this.openSnackBar("The button was deleted", "OK");
        }
      }
    )
  }

  setStatusToggles(){
    if (this.door.status == "ON" && this.refDoor.checked==false) {
      console.log("on door")
      this.refDoor.toggle();
    }
    if (this.window.status == "ON" && this.refWindow.checked==false) {
      console.log("on window")
      this.refWindow.toggle();
    }
    if (this.door.status == "OFF" && this.refDoor.checked) {
      console.log("off door")
      this.refDoor.toggle();
    }
    if (this.window.status == "OFF" && this.refWindow.checked) {
      console.log("off window")
      this.refWindow.toggle();
    }
  }
  changeRoom(selected: MatListOption[]) {
    this.locations.forEach(location => {
      if (location.name == selected[0].value) {
        this.currentLocation = location;
        this.roomService.getRooms(this.user.id, this.currentLocation.id).subscribe(rooms => {
          this.rooms = rooms;
          this.roomLength=rooms.length;
          if(this.rooms.length!=0) {
            this.currentRoom = rooms[0];
            this.gsmService.getGSMs(this.user.id, this.currentRoom.id).subscribe(gsms => {
              //fix this later
              if (gsms[0].type == "door") {
                this.door = gsms[0];
                this.window = gsms[1];
              } else {
                this.door = gsms[1];
                this.window = gsms[0];
              }
              this.setStatusToggles()
            })
          }
          else {
            this.openSnackBar(this.currentLocation.name+" has no rooms.","Ok");
            this.currentRoom = new Room(0, 0, "Sorry. There are no rooms here.");
            this.gsms.length=0;
          }
        });
      }
    });
  }

  changeControllers(selected: MatListOption[]) {
    this.rooms.forEach(room => {
      if (room.name == selected[0].value) {
        this.currentRoom = room;
        this.gsmService.getGSMs(this.user.id, this.currentRoom.id).subscribe(gsms => {
          this.gsms=gsms;
          if(gsms.length==0)
            this.openSnackBar(this.currentRoom.name+" has no controllers","Ok");
          //fix this later
          if(gsms.length==2) {
            if (gsms[0].type == "door") {
              this.door = gsms[0];
              this.window = gsms[1];
            } else {
              this.door = gsms[1];
              this.window = gsms[0];
            }
            this.setStatusToggles()
          }
        })
      }
    });
  }
}
