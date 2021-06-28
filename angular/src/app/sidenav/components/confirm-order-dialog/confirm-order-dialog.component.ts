import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BookingInfo } from 'app/shared/backend-models/interfaces';
import { Order } from 'app/sidenav/models/order.model';
import * as moment from 'moment';

@Component({
  selector: 'app-confirm-order-dialog',
  templateUrl: './confirm-order-dialog.component.html',
  styleUrls: ['./confirm-order-dialog.component.scss'],
})
export class ConfirmOrderDialogComponent implements OnInit {
  booking: any;
  orders: Order[];
  totalPrice: number;
  date: string;

  displayedColumns = ['order', 'extras', 'amount', 'price'];

  constructor(
    private dialog: MatDialogRef<ConfirmOrderDialogComponent>,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
  ) {
    this.booking = dialogData.booking;
    this.orders = dialogData.orders;
    this.totalPrice = dialogData.totalPrice;
  }

  toCurrency(n: number): string {
    if (n) {
      return Number(n).toFixed(2);
    }
    return '';
  }

  ngOnInit(): void {
    this.date = moment(this.booking.bookingDate).format('LLL');
  }

  close(val: boolean): void {
    this.dialog.close(val);
  }
}
