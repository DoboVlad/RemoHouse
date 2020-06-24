import { Component, OnInit } from '@angular/core';
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {MatSnackBar} from "@angular/material/snack-bar";

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

  constructor(public snackBar: MatSnackBar) {
    setInterval(() => {
      this.CurrentDate= new Date();
    },1);
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
    return "locatie";
  }

  getRoomName() {
    return "camera";
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

  getDate() {
    return "DATE&HOUR"
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
    fetch('http://api.openweathermap.org/data/2.5/weather?q=Bucharest&appid=2ab187c4fc0fb4ea8bb6308cfb4d2324')
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
}
