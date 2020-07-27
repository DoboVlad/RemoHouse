import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatSlideToggle, MatSlideToggleChange} from '@angular/material/slide-toggle';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatDialog} from '@angular/material/dialog';
import {DeleteButtonDialogComponent} from '../delete-button-dialog/delete-button-dialog.component';
import {Router} from '@angular/router';
import {GSMController} from '../../model/GSMController';
import {Room} from '../../model/Room';
import {LocationService} from '../../service/locationService';
import {RoomService} from '../../service/roomService';
import {GsmControllerService} from '../../service/gsmControllerService';
import {User} from '../../model/user';
import {UserService} from '../../service/userService';
import {LocationModel} from '../../model/LocationModel';
import {MatListOption, MatSelectionList} from '@angular/material/list';
import {Observable, Subject} from 'rxjs';
import {combineAll} from 'rxjs/operators';

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
  @ViewChild('door') refDoor: MatSlideToggle;
  @ViewChild('window') refWindow: MatSlideToggle;
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
  imageLoaded: Promise<boolean>;

  imageExists = true;

  constructor(public snackBar: MatSnackBar, private router: Router, private dialog: MatDialog, private locationService: LocationService,
              private roomService: RoomService, private gsmService: GsmControllerService,
              private userService: UserService,
              private cdr: ChangeDetectorRef) {
    setInterval(() => {
      this.CurrentDate = new Date();
    }, 1);
    this.locationsLoaded = new Promise<boolean>( resolve => {
      this.usersObservable = this.userService.getUserByCredential(localStorage.getItem('user'));
      this.userService.getUserByCredential(localStorage.getItem('user')).subscribe(user => {
        this.user = user;
        this.locationService.getLocations(user.id).subscribe(locations => {
          resolve();
          this.locations = locations;
          console.log(`got location`);
        });
      });
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
    this.locationsLoaded.then(() => {
      this.locationsObservable = this.locationService.getLocations(this.user.id);
    });
  }

  getLocationName() {
    if (this.currentLocation) {
      return this.currentLocation.name;
    }
  }

  getRoomName() {
    if (this.currentRoom) {
      return this.currentRoom.name;
    }
  }

  getImage(){
    return this.image;
  }

  setImage() {
    this.gsmsLoaded.then(() => {
      this.imageLoaded = new Promise<boolean>(resolve => {
        if (this.gsms.length > 0) {
          try{
            console.log(`id ${this.door.id} type ${this.door.type} status ${this.door.status}`);
            console.log(`id ${this.window.id} type ${this.window.type} status ${this.window.status}`);
            if (this.door.status == 'ON' && this.window.status == 'ON') {
              this.image = `assets/${this.currentLocation.name}/${this.currentRoom.name}/openHouse.png`;
            } else if (this.door.status == 'ON' && this.window.status == 'OFF') {
              this.image = `assets/${this.currentLocation.name}/${this.currentRoom.name}/openDoor.png`;
            } else if (this.door.status == 'OFF' && this.window.status == 'ON') {
              this.image = `assets/${this.currentLocation.name}/${this.currentRoom.name}/openWindow.png`;
            } else if (this.door.status == 'OFF' && this.window.status == 'OFF'){
              this.image = `assets/${this.currentLocation.name}/${this.currentRoom.name}/closedHouse.png`;
            }
            else {
              this.image = null;
            }
            resolve(true);
          }
          catch (e) {
            this.image = null;
            resolve(false);
          }
        }
        if (this.image !== null){
          resolve(false);
          this.imageExists = true;
        }
      });
    });

  }

  getWeatherData() {
    const formatUrl = (lat, long) => `http://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=2ab187c4fc0fb4ea8bb6308cfb4d2324`;
    const url = formatUrl(this.currentLocation.latitude, this.currentLocation.longitude);
    fetch(url)
      .then(response => response.json())
      .then(data => {this.setWeatherData(data); });
  }

  setWeatherData(data) {
    this.WeatherData = data;
    const sunsetTime = new Date(this.WeatherData.sys.sunset * 1000);
    const sunriseTime = new Date(this.WeatherData.sys.sunrise * 1000);
    this.WeatherData.sunset_time = sunsetTime.toLocaleTimeString();
    const currentDate = new Date();
    this.WeatherData.isDay = (currentDate.getTime() < sunsetTime.getTime() && currentDate.getTime() > sunriseTime.getTime());
    this.WeatherData.temp_celsius = (this.WeatherData.main.temp - 273.15).toFixed(0);
    this.WeatherData.temp_min = (this.WeatherData.main.temp_min - 273.15).toFixed(0);
    this.WeatherData.temp_max = (this.WeatherData.main.temp_max - 273.15).toFixed(0);
    this.WeatherData.temp_feels_like = (this.WeatherData.main.feels_like - 273.15).toFixed(0);
  }

  isUserLoggedIn() {
    if (localStorage.getItem('user') == 'null') {
      this.router.navigate(['/unauthorizedaccess']);
      return false;
    }
    return true;
  }

  changeRoom(selected: MatListOption[]) {
    this.locationsLoaded.then(() => {
      this.roomsLoaded = new Promise<boolean>(resolve => {
        this.gsmsLoaded = new Promise<boolean>(resolve1 => {
          this.locations.forEach(location => {
            if (location.name == selected[0].value) {
              this.currentLocation = location;
              console.log('changed location');
              this.getWeatherData();
              this.roomsObservable = this.roomService.getRooms(this.user.id, this.currentLocation.id);
              this.roomService.getRooms(this.user.id, this.currentLocation.id).subscribe(rooms => {
                this.rooms = rooms;
                this.roomLength = rooms.length;
                if (this.rooms.length != 0) {
                  resolve(true);
                  this.currentRoom = null;
                }
                // we don't need this anymore
                /*
                else {
                  resolve(false);
                  this.currentRoom = new Room(0, 0, "Sorry. There are no rooms here.");
                }
                */
                this.gsms = [];
                this.setImage();
                resolve1();
              });
            }
          });
        });
      });
    });
  }

  changeControllers(selected: MatListOption[]) {
    this.roomsLoaded.then(() => {
      this.gsmsLoaded = new Promise<boolean>(resolve => {
        this.startLoading = new Promise<boolean>(resolve1 => {
          this.rooms.forEach(room => {
            if (room.name == selected[0].value) {
              this.currentRoom = room;
              resolve1(true);
              this.controllersObservable = this.gsmService.getGSMs(this.user.id, this.currentRoom.id);
              this.gsmService.getGSMs(this.user.id, this.currentRoom.id).subscribe(gsms => {
                this.gsms = gsms;
                console.log(gsms);
                if (gsms.length == 0) {
                  this.openSnackBar(this.currentRoom.name + ' has no controllers', 'Ok');
                  resolve(false);
                }
                // fix this later
                if (gsms.length == 2) {
                  this.door = undefined;
                  this.window = undefined;
                  resolve(true);
                  this.setImage();
                }
              });
            }
          });
        });
      });
    });
  }

  controllerToggled(controller: GSMController) {
    if (controller.type == 'door'){
      this.door = controller;
    }
    else{
      this.window = controller;
    }
    this.setImage();
  }
  // kinda cheap, but it works :/
  hasRooms(){
    return this.roomsObservable && this.roomLength != 0;
  }

  hasGSM(){
    // console.log(this.gsms);
    return this.gsms && this.gsms.length != 0;
  }
}
