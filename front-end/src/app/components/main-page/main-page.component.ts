import { Component, OnInit } from '@angular/core';
import {MatSlideToggleChange} from "@angular/material/slide-toggle";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  door1 = false;
  window1 = false;
  CurrentDate = new Date();

  constructor() { }

  ngOnInit(): void {
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
    if($event.source.id==="door1"){
      this.door1 = $event.checked;
    }
    else if($event.source.id==="window1"){
      this.window1 = $event.checked;
    }
  }
}
