import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, NgForm, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { TranslocoService } from '@ngneat/transloco';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { ConfigService } from '../../core/config/config.service';
import {
  FilterCockpit,
  Pageable,
  Sort as ISort,
  TranslationToken,
} from '../../shared/backend-models/interfaces';
import { OrderListView } from '../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { OrderDialogComponent } from './order-dialog/order-dialog.component';
import * as _ from 'lodash';
import { ActivatedRoute } from '@angular/router';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { xor } from 'lodash';

@Component({
  selector: 'app-cockpit-order-cockpit',
  templateUrl: './order-cockpit.component.html',
  styleUrls: ['./order-cockpit.component.scss'],
})
export class OrderCockpitComponent implements OnInit, OnDestroy {
  private translocoSubscription = Subscription.EMPTY;
  private orderStatusTranslocoSubscription = Subscription.EMPTY;
  private paymentStatusTranslocoSubscription = Subscription.EMPTY;

  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };
  private sorting: ISort[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  orders: OrderListView[] = [];
  totalOrders: number;

  columns: TranslationToken[];

  rowColor: '';

  displayedColumns: string[] = [
    'booking.bookingDate',
    'booking.serveTime',
    'booking.tableId',
    'booking.name',
    // 'booking.email',
    // 'booking.bookingToken',
    'booking.bookingType',
    'booking.orderStatus',
    // 'booking.paymentStatus',
    'booking.actions',
  ];

  displayedColumnsArchive: string[] = [
    'booking.bookingDate',
    // 'booking.serveTime',
    'booking.name',
    // 'booking.tableId',
    // 'booking.email',
    // 'booking.bookingToken',
    'booking.orderStatus',
    // 'booking.paymentStatus',
    // 'booking.actions'
  ];

  pageSizes: number[];

  filters: FilterCockpit = {
    bookingDate: undefined,
    email: undefined,
    bookingToken: undefined,
    orderstatus: undefined,
    paymentstatus: undefined,
    name: undefined,
    table: undefined,
    bookingType: undefined,
  };

  archiveMode = false;
  title = 'cockpit.orders.title';

  orderStatusSelected: FormControl;
  paymentStatusSelected: FormControl;
  bookingTypeSelected: FormControl;

  undoValues: UndoToken[] = [];

  exludeOnRowClick: string[] = ['cancelOrder', 'advanceOrder', 'switchPayment'];

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    private configService: ConfigService,
    private route: ActivatedRoute,
    private snack: SnackBarService,
  ) {
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      if (data.archive === true) {
        // Set Archive mod settings
        this.archiveMode = true;
        this.title = 'cockpit.orders.archive';
        this.filters.paymentstatus = [0, 1]; // Payed, Refunded
        this.filters.orderstatus = [5, 6]; // Completed, Canceled
        this.displayedColumns = this.displayedColumnsArchive; // Replace active colums with archive settings
      } else {
        this.archiveMode = false;
        this.title = 'cockpit.orders.title';
        this.filters.paymentstatus = [0, 1]; // Pending, Payed
        this.filters.orderstatus = [0, 1, 2, 3, 4];
      }
      this.applyFilters();
    });
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      this.getTranslationSubscriptions(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.orderStatusSelected = new FormControl(this.filters.orderstatus, [
      Validators.required,
    ]);
    this.paymentStatusSelected = new FormControl(this.filters.paymentstatus, [
      Validators.required,
    ]);
    this.bookingTypeSelected = new FormControl(this.filters.bookingType, []);
  }

  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columns = [
          { name: 'booking.bookingDate', label: cockpitTable.reservationDateH },
          { name: 'booking.name', label: cockpitTable.nameH },
          { name: 'booking.email', label: cockpitTable.emailH },
          { name: 'booking.bookingToken', label: cockpitTable.bookingTokenH },
          { name: 'booking.serveTime', label: cockpitTable.serveTimeH },
          { name: 'booking.bookingStatus', label: cockpitTable.bookingStateH },
          { name: 'booking.paymentStatus', label: cockpitTable.paymentStateH },
          { name: 'booking.actions', label: cockpitTable.actionsH },
          { name: 'booking.tableId', label: cockpitTable.tableIdH },
          { name: 'booking.bookingType', label: cockpitTable.bookingTypeH },
        ];
      });
  }

  /** Establish Observer Subscription for Order- and Paymentstatus translations on WaiterCockpitService
   * @param lang - The language to use
   */
  getTranslationSubscriptions(lang: string): void {
    this.orderStatusTranslocoSubscription =
      this.waiterCockpitService.updateOrderStatusTranslation(
        this.translocoService,
        lang,
      );
    this.paymentStatusTranslocoSubscription =
      this.waiterCockpitService.updatePaymentStatusTranslation(
        this.translocoService,
        lang,
      );
  }

  /** Get Order Status translation from WaiterCockpitService
   * @returns the translation array
   */
  getOrderStatusTranslation(): TranslationToken[] {
    return this.waiterCockpitService.orderStatusTranslation;
  }

  /** Get Payment Status translation from WaiterCockpitService
   * @returns the translation array
   */
  getPaymentStatusTranslation(): TranslationToken[] {
    return this.waiterCockpitService.paymentStatusTranslation;
  }

  /**
   * Slices the Order Status translation array for regular or archive mode
   * @returns The sliced translation array
   */
  getOrderStatusTranslationSlice(): TranslationToken[] {
    if (!this.archiveMode) {
      return this.getOrderStatusTranslation().slice(0, -2);
    } else {
      return this.getOrderStatusTranslation().slice(-2);
    }
  }

  /**
   * Slices the Payment Status translation array for regular or archive mode
   * @returns The sliced translation array
   */
  getPaymentStatusTranslationSlice(): TranslationToken[] {
    if (!this.archiveMode) {
      return this.getPaymentStatusTranslation().slice(0, 2);
    } else {
      return this.getPaymentStatusTranslation().slice(1);
    }
  }

  /** Get Orders from backend meeting current filter requirements */
  applyFilters(): void {
    this.waiterCockpitService
      .getOrders(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.orders = [];
        } else {
          this.orders = data.content;
        }
        this.totalOrders = data.totalElements;
      });
  }

  /** Clear filters */
  clearFilters(): void {
    this.filters = {
      bookingDate: undefined,
      email: undefined,
      bookingToken: undefined,
      orderstatus: undefined,
      paymentstatus: undefined,
      name: undefined,
      table: undefined,
      bookingType: undefined,
    } as FilterCockpit;
    this.ngOnInit();
    this.pagingBar.firstPage();
  }

  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
    };
    this.applyFilters();
  }

  sort(sortEvent: Sort): void {
    this.sorting = [];
    if (sortEvent.direction) {
      this.sorting.push({
        property: sortEvent.active,
        direction: '' + sortEvent.direction,
      });
    }
    this.applyFilters();
  }

  /** Open the OrderDialogComponent for when clicking on a row
   * @param event - The HTML Event
   * @param selection - The selected Order
   */
  selected(event, selection: OrderListView): void {
    let found = false;
    this.exludeOnRowClick.forEach((value) => {
      if (event.target.className.includes(value)) {
        found = true;
        console.log(value);
      }
    });

    if (!found) {
      // Exclude action buttons
      this.dialog
        .open(OrderDialogComponent, {
          width: '80%',
          data: { selection, parrent: this },
        })
        .afterClosed()
        .subscribe((data: boolean) => {
          if (data === true) {
            // Reload orders if dialog was edited
            this.applyFilters();
          }
        });
    }
  }

  /**
   * Applies order and payment status to order
   * @param element The element of the order
   * @param orderStatus The new order status (null => skip)
   * @param paymentStatus The new payment status (null => skip)
   * @param pushUndo Whether the old value should be pushed onto the undo stack (Default=true)
   */
  applyChanges(
    element: any,
    orderStatus: number,
    paymentStatus: number,
    pushUndo: boolean = true,
  ): void {
    if (pushUndo) {
      this.undoValues.push({
        // Place old values onto undo stack
        id: element.order.id,
        orderStatus: element.order.orderStatus,
        paymentStatus: element.order.paymentStatus,
      });
    }

    if (orderStatus !== null) {
      // Send order status
      this.waiterCockpitService
        .setOrderStatus(element.order.id, orderStatus)
        .subscribe(
          ($data) => {
            this.applyFilters();
            this.snack.openSnack(
              this.translocoService.translate(
                'cockpit.status.changeStatusSuccess',
              ),
              6000,
              'green',
            );
          },
          (error) => {
            this.applyChanges(element, orderStatus, null, false); // Retry on error
          },
        );
    }

    if (paymentStatus !== null) {
      // Send payment status
      this.waiterCockpitService
        .setPaymentStatus(element.order.id, paymentStatus)
        .subscribe(
          ($data) => {
            this.applyFilters();
            this.snack.openSnack(
              this.translocoService.translate(
                'cockpit.status.changeStatusSuccess',
              ),
              6000,
              'green',
            );
          },
          (error) => {
            this.applyChanges(element, null, paymentStatus, false); // Retry on error
          },
        );
    }
  }

  /**
   * Undo the last performed action (includes changes through dialog)
   */
  undoLastChange(): void {
    // Send order status
    this.waiterCockpitService
      .setOrderStatus(
        this.undoValues[this.undoValues.length - 1].id,
        this.undoValues[this.undoValues.length - 1].orderStatus,
      )
      .subscribe((dataA: any) => {
        // Send payment status
        this.waiterCockpitService
          .setPaymentStatus(
            this.undoValues[this.undoValues.length - 1].id,
            this.undoValues[this.undoValues.length - 1].paymentStatus,
          )
          .subscribe((dataB: any) => {
            this.applyFilters();
            this.snack.openSnack(
              this.translocoService.translate(
                'cockpit.status.undoChangeSuccess',
              ),
              6000,
              'green',
            );
            this.undoValues.pop();
          });
      });
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
    this.orderStatusTranslocoSubscription.unsubscribe();
    this.paymentStatusTranslocoSubscription.unsubscribe();
  }
}

class UndoToken {
  id: number;
  orderStatus: number;
  paymentStatus: number;
}
