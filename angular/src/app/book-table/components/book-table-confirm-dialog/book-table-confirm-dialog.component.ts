import { ApplicationRef, ChangeDetectorRef, Component, Inject, NgZone, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BookingView } from 'app/shared/view-models/interfaces';
import * as moment from 'moment';
import { TranslocoService } from '@ngneat/transloco';
import { SidenavService } from 'app/sidenav/services/sidenav.service';
import { SidenavComponent } from 'app/sidenav/container/sidenav/sidenav.component';
import { ClipboardModule } from '@angular/cdk/clipboard'

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
    private applicationRef: ApplicationRef,
  ) {
    this.data = dialogData.bookingResponse;
  }

  ngOnInit(): void {
    SidenavComponent.bookingIdValue = this.data.bookingToken;
  }

  close(): void {
    this.dialog.close(true);
  }

}
