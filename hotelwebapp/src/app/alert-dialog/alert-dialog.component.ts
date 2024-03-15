import { Component, Inject } from '@angular/core';
// @ts-ignore
import {MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import {Router} from "@angular/router";

@Component({
  selector: 'app-alert-dialog',
  templateUrl: './alert-dialog.component.html',
  styleUrl: './alert-dialog.component.scss'
})
export class AlertDialogComponent {
  message: string = ""
  cancelButtonText = "Cancel";
  linkValue: string = ""

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    @Inject(MatDialogRef) private dialogRef: MatDialogRef<AlertDialogComponent>,private router:Router) {
    if (data) {
      this.message = data.message || this.message;
      this.cancelButtonText = data.buttonText || this.cancelButtonText;
      this.linkValue = data.linkValue;
    }
   // this.dialogRef.updateSize('300vw','300vw')
  }

  onConfirmClick(): void {
    this.dialogRef.close(true);
  }

  handleRouting():void{
    if(this.linkValue)
      this.router.navigate([this.linkValue]);
    else
      window.location.reload();
  }
}
