import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { BookingView, OrderListView, OrderView, SaveOrderResponse } from '../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import * as _ from 'lodash';

@Component({
  selector: 'app-cockpit-order-dialog',
  templateUrl: './order-dialog.component.html',
  styleUrls: ['./order-dialog.component.scss'],
})
export class OrderDialogComponent implements OnInit {
  private fromRow = 0;
  private currentPage = 1;

  pageSize = 4;

  data: OrderDialogData = new OrderDialogData();
  datat: OrderDialogData[] = [];
  columnst: any[];
  displayedColumnsT: string[] = [
    'bookingDate',
    'creationDate',
    'name',
    'email',
    'tableId',
    'status'
  ];

  datao: OrderView[] = [];
  columnso: any[];
  displayedColumnsO: string[] = [
    'dish.name',
    'orderLine.comment',
    'extras',
    'orderLine.amount',
    'dish.price',
  ];

  pageSizes: number[];
  filteredData: OrderView[] = this.datao;
  totalPrice: number;

  statusNamesMap: string[];
  paymentNamesMap: string[];
  selectedStatus: number;
  selectedPayment: number;

  constructor(
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
  ) {
    this.data.orderLines = dialogData.orderLines;
    this.data.booking = dialogData.booking;
    this.data.order = dialogData.order;
    this.pageSizes = this.configService.getValues().pageSizesDialog;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      this.setStatusNamesMap(event);
    });

    this.totalPrice = this.waiterCockpitService.getTotalPrice(
      this.data.orderLines,
    );
    this.datao = this.waiterCockpitService.orderComposer(this.data.orderLines);
    this.datat.push(this.data);
    this.filter();
    this.selectedStatus = this.data.order.orderstatus;
  }

  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columnst = [
          { name: 'bookingDate', label: cockpitTable.reservationDateH },
          { name: 'creationDate', label: cockpitTable.creationDateH },
          { name: 'name', label: cockpitTable.ownerH },
          { name: 'email', label: cockpitTable.emailH },
          { name: 'tableId', label: cockpitTable.tableH },
          { name: 'status', label: cockpitTable.bookingStateH}
        ];
      });

    this.translocoService
      .selectTranslateObject('cockpit.orders.dialogTable', {}, lang)
      .subscribe((cockpitDialogTable) => {
        this.columnso = [
          { name: 'dish.name', label: cockpitDialogTable.dishH },
          { name: 'orderLine.comment', label: cockpitDialogTable.commentsH },
          { name: 'extras', label: cockpitDialogTable.extrasH },
          { name: 'orderLine.amount', label: cockpitDialogTable.quantityH },
          {
            name: 'dish.price',
            label: cockpitDialogTable.priceH,
            numeric: true,
            format: (v: number) => v.toFixed(2),
          },
        ];
      });
  }

  /**Set the translation lookup array for status names */
  setStatusNamesMap(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.status', {}, lang)
      .subscribe((cockpitStatus) => {
        this.statusNamesMap = [
          cockpitStatus.recorded,
          cockpitStatus.cooking,
          cockpitStatus.ready,
          cockpitStatus.handingover,
          cockpitStatus.delivered,
          cockpitStatus.payed,
          cockpitStatus.canceled
        ]; }
      );
  }

  /**Set the translation lookup array for payment status names */
  setPaymentNamesMap(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.payment', {}, lang)
      .subscribe((cockpitStatus) => {
        this.paymentNamesMap = [
          cockpitStatus.pending,
          cockpitStatus.payed,
          cockpitStatus.refunded
        ]; }
      );
  }

  page(pagingEvent: PageEvent): void {
    this.currentPage = pagingEvent.pageIndex + 1;
    this.pageSize = pagingEvent.pageSize;
    this.fromRow = pagingEvent.pageSize * pagingEvent.pageIndex;
    this.filter();
  }

  filter(): void {
    let newData: OrderView[] = this.datao;
    newData = newData.slice(this.fromRow, this.currentPage * this.pageSize);
    setTimeout(() => (this.filteredData = newData));
  }

  increaseStatus(): void {
    this.waiterCockpitService.setOrderStatus(this.data.order.id, _.clamp(this.data.order.orderstatus + 1, 0, this.statusNamesMap.length - 2))
      .subscribe(
        (data: any) => {
          this.data.order = data;
        }
      );
  }

  applyChanges(): void {
    this.waiterCockpitService.setOrderStatus(this.data.order.id, this.selectedStatus)
      .subscribe(
        (data: any) => {
          this.data.order = data;
        }
      );
    this.waiterCockpitService.setOrderPayment(this.data.order.id, this.selectedPayment)
      .subscribe(
        (data: any) => {
          this.data.order = data;
        }
      );
  }

  cancelOrder(): void {
    this.waiterCockpitService.setOrderStatus(this.data.order.id, this.statusNamesMap.length - 1)
      .subscribe(
        (data: any) => {
          this.data.order = data;
        }
      );
  }

}

// Order Data storage class
class OrderDialogData implements OrderListView {
  orderLines: OrderView[];
  booking: BookingView;
  order: SaveOrderResponse;
}
