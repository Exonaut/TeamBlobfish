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

@Component({
  selector: 'app-cockpit-order-cockpit',
  templateUrl: './order-cockpit.component.html',
  styleUrls: ['./order-cockpit.component.scss'],
})
export class OrderCockpitComponent implements OnInit, OnDestroy {
  private translocoSubscription = Subscription.EMPTY;
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
    'booking.status',
  ];

  pageSizes: number[];

  filters: FilterCockpit = {
    bookingDate: undefined,
    email: undefined,
    bookingToken: undefined,
    state: undefined,
  };

  stateNamesMap: string[];

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    private configService: ConfigService,
  ) {
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.applyFilters();
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      this.setStateNamesMap(event);
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
          { name: 'booking.status', label: cockpitTable.bookingStateH}
        ];
      });
  }

  setStateNamesMap(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.status', {}, lang)
      .subscribe((cockpitState) => {
        this.stateNamesMap = [
          cockpitState.recorded,
          cockpitState.cooking,
          cockpitState.ready,
          cockpitState.handingover,
          cockpitState.delivered,
          cockpitState.payed,
          cockpitState.canceled
        ]}
      );
  }

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

  selected(selection: OrderListView): void {
    this.dialog.open(OrderDialogComponent, {
      width: '80%',
      data: selection,
    });
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }
}
