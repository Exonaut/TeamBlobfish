import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { BookTableService } from 'app/book-table/services/book-table.service';
import { BookingInfo } from 'app/shared/backend-models/interfaces';
import * as moment from 'moment';
import * as fromApp from '../../../store/reducers';
import * as bookTableActions from '../../store/actions/book-table.actions';

@Component({
  selector: 'app-public-book-table-dialog',
  templateUrl: './book-table-dialog.component.html',
  styleUrls: ['./book-table-dialog.component.scss'],
})
export class BookTableDialogComponent implements OnInit {
  data: BookingInfo;
  date: string;

  constructor(
    private bookingService: BookTableService,
    private store: Store<fromApp.State>,
    private dialog: MatDialogRef<BookTableDialogComponent>,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
  ) {
    this.data = this.bookingService.composeBooking(dialogData);
  }

  ngOnInit(): void {
    this.date = moment(this.data.booking.bookingDate).format('LLL');
  }

  sendBooking(): void {
    this.store.dispatch(bookTableActions.bookTable({ booking: this.data.booking }));
    this.dialog.close(true);
  }

}
