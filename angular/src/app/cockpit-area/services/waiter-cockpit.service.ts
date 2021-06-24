import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TranslocoService } from '@ngneat/transloco';
import {
  FilterCockpit,
  Pageable,
  Sort,
} from 'app/shared/backend-models/interfaces';
import { cloneDeep, map } from 'lodash';
import { Observable, Subscription } from 'rxjs';
import { exhaustMap } from 'rxjs/operators';
import { ConfigService } from '../../core/config/config.service';
import {
  BookingResponse,
  OrderResponse,
  OrderView,
  OrderViewResult,
} from '../../shared/view-models/interfaces';
import { PriceCalculatorService } from '../../sidenav/services/price-calculator.service';

@Injectable()
export class WaiterCockpitService {
  private readonly getReservationsRestPath: string =
    'bookingmanagement/v1/booking/search';
  private readonly getOrdersRestPath: string =
    'ordermanagement/v1/order/search';
  private readonly filterOrdersRestPath: string =
    'ordermanagement/v1/order/search';
  private readonly setOrderStatePath: string =
    'ordermanagement/v1/order/setstatus';
  private readonly setOrderPaymentPath: string =
    'ordermanagement/v1/order/setpayment';

  private readonly restServiceRoot$: Observable<
    string
  > = this.config.getRestServiceRoot();

  orderStatusTranslation: string[];
  paymentStatusTranslation: string[];

  constructor(
    private http: HttpClient,
    private priceCalculator: PriceCalculatorService,
    private config: ConfigService,
  ) {}

  getOrders(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterCockpit,
  ): Observable<OrderResponse[]> {
    let path: string;
    filters.pageable = pageable;
    filters.pageable.sort = sorting;

    if (filters.bookingToken === null) filters.bookingToken = undefined;
    if (filters.bookingDate === null) filters.bookingDate = undefined;
    if (filters.bookingType === null) filters.bookingType = undefined;
    if (filters.email === null) filters.email = undefined;
    if (filters.name === null) filters.name = undefined;
    if (filters.table === null) filters.table = undefined;

    path = this.filterOrdersRestPath;
    
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<OrderResponse[]>(`${restServiceRoot}${path}`, filters),
      ),
    );
  }

  /**
   * Updates the Order Status of an Order
   * @param id - The ID of the order to modify
   * @param newStatus - The new Order Status to set
   * @returns An Observable of the http-Request
   */
  setOrderStatus(
    id: number,
    newStatus: number
  ): Observable<any> {
    let path: string;
    path = this.setOrderStatePath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.patch(`${restServiceRoot}${path}/${id}/${newStatus}`, null),
      ),
    );
  }

  /**
   * Updates the Payment Status of an Order
   * @param id - The ID of the order to modify
   * @param newStatus - The new Payment Status to set
   * @returns An Observable of the http-Request
   */
  setPaymentStatus(
    id: number,
    newPayment: number
  ): Observable<any> {
    let path: string;
    path = this.setOrderPaymentPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.patch(`${restServiceRoot}${path}/${id}/${newPayment}`, null),
      ),
    );
  }

  getReservations(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterCockpit,
  ): Observable<BookingResponse[]> {
    filters.pageable = pageable;
    filters.pageable.sort = sorting;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<BookingResponse[]>(
          `${restServiceRoot}${this.getReservationsRestPath}`,
          filters,
        ),
      ),
    );
  }

  orderComposer(orderList: OrderView[]): OrderView[] {
    const orders: OrderView[] = cloneDeep(orderList);
    map(orders, (o: OrderViewResult) => {
      o.dish.price = this.priceCalculator.getPrice(o);
      o.extras = map(o.extras, 'name').join(', ');
    });
    return orders;
  }

  /** Establishes a subscription to the Order Status Translation and sets the services translation array
   * @param TranslocoService - The TranslocoService to use
   * @param lang - The language to use
   * @returns The Subscription to the Observable
   */
  updateOrderStatusTranslation(translocoService: TranslocoService, lang: string): Subscription {
    return translocoService
    .selectTranslateObject('cockpit.status', {}, lang)
    .subscribe((cockpitStatus) => {
      this.orderStatusTranslation = [
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

  /** Establishes a subscription to the Payment Status Translation and sets the services translation array
   * @param TranslocoService - The TranslocoService to use
   * @param lang - The language to use
   * @returns The Subscription to the Observable
   */
  updatePaymentStatusTranslation(translocoService: TranslocoService, lang: string): Subscription {
    return translocoService
      .selectTranslateObject('cockpit.payment', {}, lang)
      .subscribe((cockpitStatus) => {
        this.paymentStatusTranslation = [
          cockpitStatus.pending,
          cockpitStatus.payed,
          cockpitStatus.refunded
        ]; }
      );
  }

  getTotalPrice(orderLines: OrderView[]): number {
    return this.priceCalculator.getTotalPrice(orderLines);
  }
}
