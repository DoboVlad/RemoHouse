import {AfterViewInit, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {MatSlideToggle, MatSlideToggleChange} from "@angular/material/slide-toggle";
import {GSMController} from "../../model/GSMController";
import {DeleteButtonDialogComponent} from "../delete-button-dialog/delete-button-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Observable} from "rxjs";
import {GsmControllerService} from "../../service/gsmControllerService";
import {User} from "../../model/user";
import {UserService} from "../../service/userService";

@Component({
  selector: 'app-controller-toggle',
  templateUrl: './controller-toggle.component.html',
  styleUrls: ['./controller-toggle.component.css']
})
export class ControllerToggleComponent implements OnInit, AfterViewInit {

  @Input() controller: GSMController;
  @Output() notify: EventEmitter<GSMController> = new EventEmitter<GSMController>();
  @ViewChild('controllerToggle') toggle : MatSlideToggle;
  user: User;

  constructor(private dialog: MatDialog, public snackBar: MatSnackBar, private gsmService: GsmControllerService, private userService: UserService) { }

  ngOnInit(): void {
    this.userService.getUserByCredential(localStorage.getItem("user")).subscribe(user => {
      this.user = user;
    });
  }


  deleteButton() {
    const dialogRef = this.dialog.open(DeleteButtonDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
        if (result == true) { //fix this
          this.gsmService.deleteGSM(this.user.id, this.controller.id);
          this.openSnackBar("The controller was deleted", "OK");
        }
      }
    )
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
      verticalPosition: 'bottom',
      horizontalPosition: 'left'

    });
  }

  doorChange($event: MatSlideToggleChange) {
    if(this.controller.status == "OFF") {
      this.gsmService.openGSM(this.controller, this.user.id).subscribe(response => {
        if (response) {
          this.openSnackBar(`Opened ${this.controller.type}`, "OK");
        } else {
          this.openSnackBar("Something went wrong", "OK");
          this.toggle.toggle();
        }
      });
    }
    else if(this.controller.status == "ON"){
      this.gsmService.closeGSM(this.controller, this.user.id).subscribe(response => {
        if (response) {
          this.openSnackBar(`Closed ${this.controller.type}`, "OK");

        } else {
          this.openSnackBar("Something went wrong", "OK");
          this.toggle.toggle();
        }
      });
    }
    if(this.controller.status == "ON"){
      this.controller.status = "OFF";
    }
    else{
      this.controller.status = "ON";
    }
    this.notify.emit(this.controller)
  }

  ngAfterViewInit(): void {
    console.log(`entered after init ${this.controller.id} ${this.controller.type} ${this.controller.status}`);
    this.notify.emit(this.controller);
    if(this.controller.status==="ON"){
      this.toggle.toggle();
    }
  }
}
