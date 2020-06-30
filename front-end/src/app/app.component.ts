import {Component, OnInit} from '@angular/core';
import {NavigationStart, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'REMO';

  constructor(private router: Router) {
    router.events.subscribe((val)=>{
      if(val instanceof  NavigationStart && router.navigated){
        const x = document.getElementById("nav");
        if (x.className === "navbar") {
          x.className += " responsive";
        } else {
          x.className = "navbar";
        }
      }
    });
  }

  ngOnInit(): void {
    //responsive navbar for mobile
    function show() {
      const x = document.getElementById("nav");
      if (x.className === "navbar") {
        x.className += " responsive";
      } else {
        x.className = "navbar";
      }
    }

    const icon = document.getElementById("icon");
    icon.addEventListener("click", show);

    //sticky navbar
    window.onscroll = function() {fixNav()};

    var header = document.getElementById("nav");

    var sticky = header.offsetTop;

    function fixNav() {
      if (window.pageYOffset > sticky) {
        header.classList.add("sticky");
      } else {
        header.classList.remove("sticky");
      }
    }
  }

  isUserLoggedIn() {
    return localStorage.getItem("user")!="null";
  }
}
