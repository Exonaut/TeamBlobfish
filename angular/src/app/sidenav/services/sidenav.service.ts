import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import {
  BookingInfo,
  OrderInfo,
  OrderListInfo,
} from 'app/shared/backend-models/interfaces';
import * as fromOrder from 'app/sidenav/store/selectors/order.selectors';
import * as fromApp from 'app/store/reducers';
import { Observable } from 'rxjs';
import { exhaustMap, map } from 'rxjs/operators';
import {
  ExtraView,
  SaveOrderResponse,
} from '../../shared/view-models/interfaces';
import { Order } from '../models/order.model';
import { ConfigService } from '../../core/config/config.service';
import { map as mapl } from 'lodash';
import { FormGroup } from '@angular/forms';

@Injectable()
export class SidenavService {
  private readonly restServiceRoot$: Observable<string> =
    this.config.getRestServiceRoot();
  private readonly saveOrdersPath: string = 'ordermanagement/v1/order';
  private readonly booktableRestPath: string = 'bookingmanagement/v1/booking';
  orders$: Observable<Order[]>;

  opened = false;
  openTab = 0;

  constructor(
    private http: HttpClient,
    private store: Store<fromApp.State>,
    private config: ConfigService,
  ) {}

  public openSideNav(): void {
    this.opened = true;
  }

  public closeSideNav(): void {
    this.opened = false;
  }

  public openSideNavOrder(): void {
    this.openSideNav();
    this.openTab = 0;
  }

  public openSideNavBooking(): void {
    this.openSideNav();
    this.openTab = 1;
  }

  public getNumberOrders(): Observable<number> {
    this.orders$ = this.store.select(fromOrder.getAllOrders);
    return this.orders$.pipe(map((orders) => orders.length));
  }

  /**
   * Sends order data
   * @param token The order token to use
   * @returns Observable of the HTTP request
   */
  public sendOrders(token: any): Observable<SaveOrderResponse> {
    this.orders$ = this.store.select(fromOrder.getAllOrders);
    return this.orders$.pipe(
      map((orderData) => {
        const orderList: OrderListInfo = {
          booking: { bookingToken: token.bookingToken },
          orderLines: this.composeOrders(orderData),
          order: token.address,
        };
        return orderList;
      }),
      exhaustMap((orderList) => {
        this.closeSideNav();
        return this.restServiceRoot$.pipe(
          exhaustMap((restServiceRoot) =>
            this.http.post<SaveOrderResponse>(
              `${restServiceRoot}${this.saveOrdersPath}`,
              orderList,
            ),
          ),
        );
      }),
    );
  }

  composeOrders(orders: Order[]): OrderInfo[] {
    const composedOrders: OrderInfo[] = [];
    orders.forEach((order: Order) => {
      const extras: any[] = [];
      order.details.extras.forEach((extra: ExtraView) =>
        extras.push({ id: extra.id }),
      );
      composedOrders.push({
        orderLine: {
          dishId: order.details.dish.id,
          amount: order.details.orderLine.amount,
          comment: order.details.orderLine.comment,
        },
        extras,
      });
    });
    return composedOrders;
  }

  /**
   * Sends booking data
   * @param bookInfo The booking data to use
   * @returns Observable of the HTTP request
   */
  postBooking(bookInfo: BookingInfo): Observable<any> {
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post(`${restServiceRoot}${this.booktableRestPath}`, bookInfo),
      ),
    );
  }

  composeBooking(invitationData: any): BookingInfo {
    const composedBooking: BookingInfo = {
      booking: {
        bookingDate: invitationData.bookingDate,
        name: invitationData.name,
        email: invitationData.email,
        bookingType: invitationData.bookingType,
      },
    };

    if (invitationData.bookingType === 1) {
      composedBooking.invitedGuests = mapl(
        invitationData.invitedGuests,
        (email: string) => ({ email }),
      );
    } else if (invitationData.bookingType === 0) {
      composedBooking.booking.assistants = invitationData.assistants;
    }

    return composedBooking;
  }
}
