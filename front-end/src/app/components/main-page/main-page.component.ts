import { Component, OnInit } from '@angular/core';
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {GSMController} from "../../model/GSMController";
import {Room} from "../../model/Room";
import {LocationService} from "../../service/locationService";
import {RoomService} from "../../service/roomService";
import {GsmControllerService} from "../../service/gsmControllerService";
import {User} from "../../model/user";
import {UserService} from "../../service/userService";
import {LocationModel} from "../../model/LocationModel";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  door1 = false;
  window1 = false;
  CurrentDate = new Date();
  WeatherData: any;
  location : LocationModel;
  room : Room;
  window: GSMController;
  door : GSMController;
  user : User;

  constructor(public snackBar: MatSnackBar,private router:Router, private locationService : LocationService,
              private roomService : RoomService, private gsmService:GsmControllerService,
              private userService : UserService) {
    setInterval(() =>{
      this.CurrentDate=new Date();
    },1);
    userService.getUserByCredential(localStorage.getItem("user")).subscribe(user=>{
      this.user=user;
      locationService.getLocations(user.id).subscribe(locations=>{
        this.location = locations[0];
        roomService.getRooms(user.id,this.location.id).subscribe(rooms=>{
          this.room = rooms[0];
          gsmService.getGSMs(user.id,this.room.id).subscribe(gsms=>{
            //fix this later
            this.door = gsms[0];
            this.window = gsms[1];
            console.log(this.user,this.location,this.room,this.door,this.window);
          })
        })
      })
    })

  }
  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }

  ngOnInit(): void {
    this.WeatherData ={
      main: {},
      isDay: true
    };
    this.getWeatherData();
  }

  getLocationName() {
    return this.location.name;
  }

  getRoomName() {
    return this.room.name;
  }

  getImage() {
    if(this.door1 && this.window1){
      return "assets/openHouse.png"
    }
    else if(this.door1 && !this.window1){
      return "assets/openDoor.png"
    }
    else if(!this.door1 && this.window1){
      return "assets/openWindow.png"
    }
    else{
      return "assets/closedHouse.png"
    }
  }

  onChange($event: MatSlideToggleChange) {
    // console.log($event.checked); -> true if the slide is toggled
    // console.log($event.source.id); -> id of the toggle (door1, window1)
    let message = "";
    if($event.source.id==="door1"){
      this.door1 = $event.checked;
      message = "door";
    }
    else if($event.source.id==="window1"){
      this.window1 = $event.checked;
      message = "window";
    }
    if($event.checked){
      this.openSnackBar("Opened " + message, "OK");
    }
    else{
      this.openSnackBar("Closed " + message, "OK");
    }
  }

  getWeatherData(){
    //API key 2ab187c4fc0fb4ea8bb6308cfb4d2324
    fetch('http://api.openweathermap.org/data/2.5/weather?q=lat='+this.location.latitude+'&lon='+this.location.longitude+'&appid=2ab187c4fc0fb4ea8bb6308cfb4d2324')
      .then(response => response.json())
      .then(data => {this.setWeatherData(data);});
    // let data = JSON.parse("{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"base\":\"stations\",\"main\":{\"temp\":287.329,\"pressure\":1012.69,\"humidity\":67,\"temp_min\":287.329,\"temp_max\":287.329,\"sea_level\":1020.15,\"grnd_level\":1012.69},\"wind\":{\"speed\":4.76,\"deg\":95.0004},\"clouds\":{\"all\":12},\"dt\":1476443177,\"sys\":{\"message\":0.004,\"country\":\"GB\",\"sunrise\":1476426249,\"sunset\":1476464855},\"id\":2643743,\"name\":\"London\",\"cod\":200}");
    // this.setWeatherData(data);
  }

  setWeatherData(data){
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
    if(localStorage.getItem("user")=="null") {
      this.router.navigate(["/unauthorizedaccess"]);
      return false;
    }
    return true;

  }
}
