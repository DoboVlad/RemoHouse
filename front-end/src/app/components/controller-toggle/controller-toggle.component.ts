import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatSlideToggleChange} from "@angular/material/slide-toggle";
import {GSMController} from "../../model/GSMController";
import {DeleteButtonDialogComponent} from "../delete-button-dialog/delete-button-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-controller-toggle',
  templateUrl: './controller-toggle.component.html',
  styleUrls: ['./controller-toggle.component.css']
})
export class ControllerToggleComponent implements OnInit {

  @Input() controller: GSMController;
  @Output() notify: EventEmitter<GSMController> = new EventEmitter<GSMController>();

  constructor(private dialog: MatDialog, public snackBar: MatSnackBar) { }

  ngOnInit(): void {
    console.log(this.controller.id);
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

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
      verticalPosition: 'bottom',
      horizontalPosition: 'left'

    });
  }

  doorChange($event: MatSlideToggleChange) {
    console.log("toggle bitch");
    this.controller.status = this.controller.status == 'ON' ? 'OFF' : 'ON';
    this.notify.emit(this.controller);
  }
}
