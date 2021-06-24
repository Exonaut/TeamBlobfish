import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { SnackBarService } from '../../../core/snack-bar/snack-bar.service';
import * as fromRoot from '../../../store';
import { BookTableService } from '../../services/book-table.service';
import * as bookTableActions from '../actions/book-table.actions';
import { TranslocoService } from '@ngneat/transloco';
import { BookingInfo, ReservationInfo } from 'app/shared/backend-models/interfaces';
import { BookingResponse } from 'app/book-table/models/booking-response.model';
import { BookTableComponent } from 'app/book-table/container/book-table/book-table.component';
import { MatDialog } from '@angular/material/dialog';
import { BookTableConfirmDialogComponent } from 'app/book-table/components/book-table-confirm-dialog/book-table-confirm-dialog.component';
import { WindowService } from 'app/core/window/window.service';

@Injectable()
export class BookTableEffects {
  bookTable$ = createEffect(() =>
    this.actions$.pipe(
      ofType(bookTableActions.bookTable),
      map((booking) => booking.booking),
      switchMap((booking: ReservationInfo) => {
        return this.bookTableService.postBooking({ booking }).pipe(
          map((res: BookingResponse) =>
            bookTableActions.bookTableSuccess({
              bookingResponse: {
                name: res.name,
                bookingDate: res.bookingDate,
                bookingToken: res.bookingToken,
                tableId: res.tableId,
                email: res.email,
              },
            }),
          ),
          catchError((error) => of(bookTableActions.bookTableFail({ error }))),
        );
      }),
    ),
  );

  bookTableSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(bookTableActions.bookTableSuccess),
        map((res) => {
          this.dialog
            .open(BookTableConfirmDialogComponent, {
              width: this.window.responsiveWidth(),
              data: res,
            })
            .afterClosed(),
          this.snackBar.openSnack(
            this.translocoService.translate('bookTable.dialog.bookingSuccess'),
            10000,
            'green',
          );
          fromRoot.go({ path: ['/menu'] });
        }),
      ),
    { dispatch: false },
  );

  bookTableFail$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(bookTableActions.bookTableFail),
        tap(() => {
          this.snackBar.openSnack(
            this.translocoService.translate('bookTable.dialog.bookingError'),
            4000,
            'red',
          );
        }),
      ),
    { dispatch: false },
  );

  inviteFriends$ = createEffect(() =>
    this.actions$.pipe(
      ofType(bookTableActions.inviteFriends),
      map((booking) => booking.booking),
      switchMap((booking: BookingInfo) =>
        this.bookTableService.postBooking(booking).pipe(
          map((res: any) =>
          bookTableActions.inviteFriendsSuccess({
            bookingResponse: {
              name: res.name,
              bookingDate: res.bookingDate,
              bookingToken: res.bookingToken,
              tableId: res.tableId,
              email: res.email,
            }
          }),
          ),
          catchError((error) =>
            of(
              bookTableActions.inviteFriendsFail({
                error,
              }),
            ),
          ),
        ),
      ),
    ),
  );

  inviteFriendsSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(bookTableActions.inviteFriendsSuccess),
        map((res) => {
          this.dialog
            .open(BookTableConfirmDialogComponent, {
              width: this.window.responsiveWidth(),
              data: res,
            })
            .afterClosed(),
          this.snackBar.openSnack(
            this.translocoService.translate('bookTable.dialog.bookingSuccess'),
            4000,
            'green',
          );
          fromRoot.go({ path: ['/bookTable'] });
        }),
      ),
    { dispatch: false },
  );

  inviteFriendsFail$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(bookTableActions.inviteFriendsFail),
        tap(() => {
          this.snackBar.openSnack(
            this.translocoService.translate('bookTable.dialog.bookingError'),
            4000,
            'red',
          );
        }),
      ),
    { dispatch: false },
  );

  constructor(
    private actions$: Actions,
    public translocoService: TranslocoService,
    private bookTableService: BookTableService,
    public snackBar: SnackBarService,
    private dialog: MatDialog,
    private window: WindowService,
  ) {}
}