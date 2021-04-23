import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BookingView } from 'app/shared/view-models/interfaces';
import * as moment from 'moment';
import { TranslocoService } from '@ngneat/transloco';

@Component({
  selector: 'app-book-table-confirm-dialog',
  templateUrl: './book-table-confirm-dialog.component.html',
  styleUrls: ['./book-table-confirm-dialog.component.scss']
})
export class BookTableConfirmDialogComponent implements OnInit {

  data: BookingView;
  date: string;

  constructor(
    private dialog: MatDialogRef<BookTableConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
  ) {
    this.data = dialogData.bookingResponse;
  }

  ngOnInit(): void {
    // this.date = moment(this.data.creationDate).format('LLL');
  }

  close(): void {
    this.dialog.close(true);
  }

}
