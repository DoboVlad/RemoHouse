import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'REMO';

  ngOnInit(): void {
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
}
