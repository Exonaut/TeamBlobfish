import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BookingInfo } from 'app/shared/backend-models/interfaces';

@Component({
  selector: 'app-confirm-order-dialog',
  templateUrl: './confirm-order-dialog.component.html',
  styleUrls: ['./confirm-order-dialog.component.scss']
})
export class ConfirmOrderDialogComponent implements OnInit {

  data: BookingInfo;

  constructor(
    private dialog: MatDialogRef<ConfirmOrderDialogComponent>,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
  ) { 
    this.data = dialogData;
  }

  ngOnInit(): void {
  }

  close(val: boolean): void {
    this.dialog.close(val);
  }

}
