import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { BookingView, OrderListView, OrderView, SaveOrderResponse } from '../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import * as _ from 'lodash';
import { MatSelectChange } from '@angular/material/select';

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
    'orderStatus',
    'paymentStatus'
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
    public dialog: MatDialogRef<OrderDialogComponent>,
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
      this.setPaymentNamesMap(event);
    });

    this.totalPrice = this.waiterCockpitService.getTotalPrice(
      this.data.orderLines,
    );
    this.datao = this.waiterCockpitService.orderComposer(this.data.orderLines);
    this.datat.push(this.data);
    this.filter();
    this.selectedStatus = this.data.order.orderStatus;
    this.selectedPayment = this.data.order.paymentStatus;
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
          { name: 'bookingStatus', label: cockpitTable.bookingStateH },
          { name: 'paymentStatus', label: cockpitTable.paymentStateH }
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

  /** Set the translation lookup array for status names */
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
          cockpitStatus.completed,
          cockpitStatus.canceled
        ]; }
      );
  }

  /** Set the translation lookup array for payment status names */
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
    this.selectedStatus = _.clamp(this.data.order.orderStatus + 1, 0, this.statusNamesMap.length - 2);

    if (this.selectedStatus === this.statusNamesMap.length - 2) {
      this.selectedPayment = 1;
    }

    this.applyChanges();
  }

  applyChanges(): void {
    this.waiterCockpitService.setOrderStatus(this.data.order.id, this.selectedStatus) // Send order status
      .subscribe(
        (dataA: any) => {
          this.waiterCockpitService.setPaymentStatus(this.data.order.id, this.selectedPayment) // Send payment status
            .subscribe(
              (dataB: any) => {
                this.data.order = dataB;
                this.dialog.close(true); // Close dialog with refresh flag
              }
            );
        }
      );
  }

  autoChangePaymentStatus(event: MatSelectChange): void {
    if (event.value < this.statusNamesMap.length - 2 && this.selectedPayment >= 2) { // Change to pending
      this.selectedPayment = 0;
    }
    if (event.value === this.statusNamesMap.length - 1) { // Change to refunded
      this.selectedPayment = 2;
    }
    if (event.value === this.statusNamesMap.length - 2) { // Change to payed
      this.selectedPayment = 1;
    }
  }

  autoChangeOrderStatus(event: MatSelectChange): void {
    if (event.value === 2) { // Change to canceled
      this.selectedStatus = this.statusNamesMap.length - 1;
    }
    if (event.value < 2 && this.selectedStatus >= this.statusNamesMap.length - 2) { // Change to canceled
      this.selectedStatus = 0;
    }
  }

}

// Order Data storage class
class OrderDialogData implements OrderListView {
  orderLines: OrderView[];
  booking: BookingView;
  order: SaveOrderResponse;
}
