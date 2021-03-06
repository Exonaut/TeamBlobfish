import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnInit,
} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatDialog } from '@angular/material/dialog';
import { TranslocoService } from '@ngneat/transloco';
import { Store } from '@ngrx/store';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { BookingInfo } from 'app/shared/backend-models/interfaces';
import { ConfirmDialogComponent } from 'app/shared/confirm-dialog/confirm-dialog.component';
import { emailValidator } from 'app/shared/directives/email-validator.directive';
import { ConfirmOrderDialogComponent } from 'app/sidenav/components/confirm-order-dialog/confirm-order-dialog.component';
import { BookingResponse } from 'app/sidenav/models/booking-response.model';
import { Order } from 'app/sidenav/models/order.model';
import { last } from 'lodash';
import * as moment from 'moment';
import { Observable } from 'rxjs';
import * as fromRoot from '../../../store';
import { SidenavService } from '../../services/sidenav.service';
import * as fromOrder from '../../store';

@Component({
  selector: 'app-public-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SidenavComponent implements OnInit {
  orders$: Observable<Order[]>;
  orders: Order[];
  totalPrice$: Observable<number>;
  totalPrice: number;

  delivery: boolean;

  invitationModel: string[] = [];
  minDate: Date = new Date();
  bookForm: FormGroup;
  deliveryForm: FormGroup;
  invitationForm: FormGroup;
  tokenForm: FormGroup;

  selectedTab: number;

  REGEXP_EMAIL =
    /^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;

  reservationInfo: BookingInfo = {
    booking: {
      name: '',
      email: '',
      bookingDate: undefined,
      bookingType: 0,
    },
    invitedGuests: undefined,
  };

  constructor(
    public sidenav: SidenavService,
    private store: Store<fromOrder.SideNavState>,
    private changeDetectorRef: ChangeDetectorRef,
    private translocoService: TranslocoService,
    private snackBarService: SnackBarService,
    public sidenavService: SidenavService,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.delivery = false;
    this.selectedTab = 0;

    this.orders$ = this.store.select(fromOrder.getAllOrders);
    this.store
      .select(fromOrder.getAllOrders)
      .subscribe((orders) => (this.orders = orders));
    this.totalPrice$ = this.store.select(fromOrder.getTotalPrice);
    this.totalPrice$.subscribe((price) => {
      this.totalPrice = price;
    });
    setInterval(() => {
      // Update Order Token input
      this.changeDetectorRef.markForCheck();
      this.selectedTab = this.sidenavService.openTab;
    }, 100);

    const booking = this.reservationInfo.booking;
    this.bookForm = new FormGroup({
      bookingDate: new FormControl(booking.bookingDate, Validators.required),
      name: new FormControl(booking.name, Validators.required),
      email: new FormControl(booking.email, [
        Validators.required,
        Validators.pattern(this.REGEXP_EMAIL),
      ]),
      assistants: new FormControl(booking.assistants, [
        Validators.required,
        Validators.min(1),
        Validators.max(8),
      ]),
      invitedGuests: new FormControl(this.invitationModel, [Validators.email]),
    });
    this.deliveryForm = new FormGroup({
      name: new FormControl(booking.name, Validators.required),
      email: new FormControl(booking.email, [
        Validators.required,
        Validators.pattern(this.REGEXP_EMAIL),
      ]),
      city: new FormControl(null, Validators.required),
      street: new FormControl(null, Validators.required),
      streetNr: new FormControl(null, Validators.required),
    });
    this.tokenForm = new FormGroup({
      bookingId: new FormControl(null, Validators.required),
    });
  }

  get name(): AbstractControl {
    if (!this.delivery) {
      return this.bookForm.get('name');
    } else {
      return this.deliveryForm.get('name');
    }
  }
  get email(): AbstractControl {
    if (!this.delivery) {
      return this.bookForm.get('email');
    } else {
      return this.deliveryForm.get('email');
    }
  }
  get assistants(): AbstractControl {
    return this.bookForm.get('assistants');
  }
  get bookingType(): AbstractControl {
    return this.bookForm.get('bookingType');
  }
  get invitedGuests(): AbstractControl {
    return this.bookForm.get('invitedGuests');
  }

  get bookingId(): AbstractControl {
    return this.tokenForm.get('bookingId');
  }

  get invName(): AbstractControl {
    return this.invitationForm.get('name');
  }
  get invEmail(): AbstractControl {
    return this.invitationForm.get('email');
  }

  get city(): AbstractControl {
    return this.deliveryForm.get('city');
  }
  get street(): AbstractControl {
    return this.deliveryForm.get('street');
  }
  get streetNr(): AbstractControl {
    return this.deliveryForm.get('streetNr');
  }

  clearInputs(): void {
    this.deliveryForm.reset();
    this.bookForm.reset();
    this.invitedGuests.setValue([]);
    this.store.dispatch(fromOrder.clearOrders());
  }

  closeSidenav(): void {
    this.sidenav.closeSideNav();
  }

  navigateTo(route: string): void {
    this.store.dispatch(fromRoot.go({ path: [route] }));
    this.closeSidenav();
  }

  /**
   * Sends booking information
   */
  sendBooking(): void {
    // Set correct booking type
    const booking = this.delivery ? this.deliveryForm.value : this.bookForm.value;
    if (this.delivery) {
      // Delivery
      booking.bookingType = 2;
      booking.bookingDate = this.minDate.setTime(
        this.minDate.getTime() + 1000 * 60,
      );
    } else if (booking.invitedGuests && booking.invitedGuests.length > 0) {
      // With Guest Emails
      booking.bookingType = 1;
    } else {
      // Regular
      booking.bookingType = 0;
    }

    // Placeholder data for non-delivery bookings
    if (!this.delivery) {
      booking.city = '';
      booking.street = '';
      booking.streetNr = '';
    }

    this.dialog
      .open(ConfirmOrderDialogComponent, {
        data: {booking, orders: this.orders, totalPrice: this.totalPrice},
        width: '30%',
      })
      .afterClosed()
      .subscribe((val) => {
        if (val) {
          // Send booking data
          this.sidenavService
            .postBooking(this.sidenavService.composeBooking(booking))
            .subscribe(
              ($bookingResponse: BookingResponse) => {
                if (this.orders.length > 0) {
                  // Send order data
                  this.sidenavService
                    .sendOrders({
                      address: {
                        city: booking.city,
                        street: booking.street,
                        streetNr: booking.streetNr,
                      },
                      bookingToken: $bookingResponse.bookingToken,
                    })
                    .subscribe(
                      ($orderResponse) => {
                        this.closeSidenav();
                        this.snackBarService.openSnack(
                          this.translocoService.translate(
                            'sidenav.orderSuccess',
                          ),
                          6000,
                          'green',
                        );
                      },
                      (error) => {
                        this.snackBarService.openSnack(
                          this.translocoService.translate('sidenav.orderError'),
                          6000,
                          'red',
                        );
                      },
                    );
                } else if (!this.delivery) {
                  this.closeSidenav();
                  this.snackBarService.openSnack(
                    this.translocoService.translate('sidenav.bookingSuccess'),
                    6000,
                    'green',
                  );
                } else if (this.delivery) {
                  this.closeSidenav();
                  this.snackBarService.openSnack(
                    this.translocoService.translate('sidenav.deliverySuccess'),
                    6000,
                    'green',
                  );
                }
                this.clearInputs();
              },
              (error) => {
                if (this.delivery) {
                  this.snackBarService.openSnack(
                    this.translocoService.translate('sidenav.deliveryError'),
                    6000,
                    'red',
                  );
                } else {
                  this.snackBarService.openSnack(
                    this.translocoService.translate('sidenav.bookingError'),
                    6000,
                    'red',
                  );
                }
              },
            );
        }
      });
  }

  /**
   * Send order data without create a new booking
   */
  sendOrder(): void {
    this.sidenavService
      .sendOrders({
        address: {
          city: '',
          street: '',
          streetNr: '',
        },
        bookingToken: this.tokenForm.value.bookingId,
      })
      .subscribe(
        ($orderResponse) => {
          this.closeSidenav();
          this.snackBarService.openSnack(
            this.translocoService.translate('sidenav.orderSuccess'),
            6000,
            'green',
          );
        },
        (error) => {
          this.snackBarService.openSnack(
            this.translocoService.translate('sidenav.orderError'),
            6000,
            'red',
          );
        },
      );
  }

  findOrder(id: string): Order {
    return this.orders.find((order) => order.id === id);
  }

  onOrderIncreased(order: Order): void {
    const orderToUpdate = this.findOrder(order.id);
    if (orderToUpdate) {
      this.store.dispatch(
        fromOrder.updateOrder({
          order: {
            id: order.id,
            changes: {
              details: {
                ...orderToUpdate.details,
                extras: orderToUpdate.details.extras,
                orderLine: {
                  ...orderToUpdate.details.orderLine,
                  amount: orderToUpdate.details.orderLine.amount + 1,
                },
              },
            },
          },
        }),
      );
    }
  }

  onOrderDecreased(order: Order): void {
    const orderToUpdate = this.findOrder(order.id);
    if (orderToUpdate) {
      if (orderToUpdate.details.orderLine.amount > 1) {
        this.store.dispatch(
          fromOrder.updateOrder({
            order: {
              id: order.id,
              changes: {
                details: {
                  ...orderToUpdate.details,
                  extras: orderToUpdate.details.extras,
                  orderLine: {
                    ...orderToUpdate.details.orderLine,
                    amount: orderToUpdate.details.orderLine.amount - 1,
                  },
                },
              },
            },
          }),
        );
      } else {
        // Since the amount is 0 we remove it from the store
        this.store.dispatch(fromOrder.deleteOrder({ id: order.id }));
      }
    }
  }

  onOrderRemoved(order: Order): void {
    this.store.dispatch(fromOrder.deleteOrder({ id: order.id }));
  }

  onCommentRemoved(order: Order): void {
    this.store.dispatch(
      fromOrder.updateOrder({
        order: {
          id: order.id,
          changes: {
            details: {
              dish: order.details.dish,
              orderLine: {
                amount: order.details.orderLine.amount,
                comment: null,
              },
              extras: order.details.extras,
            },
          },
        },
      }),
    );
  }

  onCommentAdded(order: Order): void {
    this.store.dispatch(
      fromOrder.updateOrder({
        order: {
          id: order.id,
          changes: {
            details: {
              dish: order.details.dish,
              orderLine: {
                amount: order.details.orderLine.amount,
                comment: order.details.orderLine.comment,
              },
              extras: order.details.extras,
            },
          },
        },
      }),
    );
  }

  getFirstDayWeek(): string {
    moment.locale(this.translocoService.getActiveLang());
    const firstDay: string = moment(moment().weekday(0)).format('d');
    return firstDay;
  }

  removeInvite(invite: string): void {
    const index = this.invitationModel.indexOf(invite);
    if (index >= 0) {
      this.invitationModel.splice(index, 1);
    }
  }

  validateEmail(event: MatChipInputEvent): void {
    this.invitationModel.push(event.value);
    event.input.value = '';
    if (!emailValidator(last(this.invitationModel))) {
      this.invitationModel.pop();
      this.snackBarService.openSnack(
        this.translocoService.translate('bookTable.formErrors.emailFormat'),
        1000,
        'red',
      );
    }
  }
}
