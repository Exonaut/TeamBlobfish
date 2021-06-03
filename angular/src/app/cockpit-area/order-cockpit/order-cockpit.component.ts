import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
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
} from '../../shared/backend-models/interfaces';
import { OrderListView } from '../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { OrderDialogComponent } from './order-dialog/order-dialog.component';
import * as _ from 'lodash';
import { ActivatedRoute } from '@angular/router';

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

  columns: any[];

  displayedColumns: string[] = [
    'booking.bookingDate',
    'booking.email',
    'booking.bookingToken',
    'booking.orderStatus',
    'booking.paymentStatus',
    'booking.actions'
  ];

  pageSizes: number[];

  filters: FilterCockpit = {
    bookingDate: undefined,
    email: undefined,
    bookingToken: undefined,
    orderstatus: [],
    paymentstatus: [],
  };

  orderStatusTranslation: any[];
  paymentStatusTranslation: any[];

  archiveMode = false;
  title = 'cockpit.orders.title';

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    private configService: ConfigService,
    private route: ActivatedRoute
  ) {
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.route.data
      .subscribe(data => {
        if (data.archive === true) // Set Archive mod settings
        {
          this.archiveMode = true;
          this.title = 'cockpit.orders.archive';
          this.filters.paymentstatus = [1, 2];
          this.filters.orderstatus = [5, 6];
          this.displayedColumns.pop();
        }
        else
        {
          this.archiveMode = false;
          this.title = 'cockpit.orders.title';
          this.filters.paymentstatus = [0, 1];
          this.filters.orderstatus = [0, 1, 2, 3, 4];
        }
        this.applyFilters();
    });
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      this.getTranslationSubscriptions(event);
      moment.locale(this.translocoService.getActiveLang());
    });
  }

  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columns = [
          { name: 'booking.bookingDate', label: cockpitTable.reservationDateH },
          { name: 'booking.email', label: cockpitTable.emailH },
          { name: 'booking.bookingToken', label: cockpitTable.bookingTokenH },
          { name: 'booking.bookingStatus', label: cockpitTable.bookingStateH},
          { name: 'booking.paymentStatus', label: cockpitTable.paymentStateH},
          { name: 'booking.actions', label: cockpitTable.actionsH},
        ];
      });
  }

  getTranslationSubscriptions(lang: string): void {
    this.orderStatusTranslocoSubscription = this.waiterCockpitService.updateOrderStatusTranslation(this.translocoService, lang);
    this.paymentStatusTranslocoSubscription = this.waiterCockpitService.updatePaymentStatusTranslation(this.translocoService, lang);
  }

  getOrderStatusTranslation(): string[] {
    return this.waiterCockpitService.orderStatusTranslation;
  }

  getPaymentStatusTranslation(): string[] {
    return this.waiterCockpitService.paymentStatusTranslation;
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
  clearFilters(filters: NgForm): void {
    filters.reset();
    this.applyFilters();
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

  selected(event, selection: OrderListView): void {
    if (!event.target.className.includes('button')) {
      this.dialog.open(OrderDialogComponent, {
        width: '80%',
        data: selection,
      }).afterClosed().subscribe((data: boolean) => {
        if (data === true) { // Reload orders if dialog was edited
          this.applyFilters();
        }
      });
    }
  }

  applyChanges(element: any, orderStatus: number, paymentStatus: number): void {
    if (orderStatus === 5) { paymentStatus = 1; }
    this.waiterCockpitService.setOrderStatus(element.order.id, orderStatus) // Send order status
      .subscribe(
        (dataA: any) => {
          this.waiterCockpitService.setPaymentStatus(element.order.id, paymentStatus) // Send payment status
            .subscribe(
              (dataB: any) => {
                this.applyFilters();
              }
            );
        }
      );
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
    this.orderStatusTranslocoSubscription.unsubscribe();
    this.paymentStatusTranslocoSubscription.unsubscribe();
  }
}
