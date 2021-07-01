import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, inject, TestBed } from '@angular/core/testing';
import { OrderView } from '../../shared/view-models/interfaces';
import { PriceCalculatorService } from '../../sidenav/services/price-calculator.service';
import { WaiterCockpitService } from './waiter-cockpit.service';
import { ConfigService } from '../../core/config/config.service';
import { config } from '../../core/config/config';
import { provideMockStore } from '@ngrx/store/testing';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CoreModule } from 'app/core/core.module';
import { TranslocoService } from '@ngneat/transloco';
import { FilterCockpit, Pageable, Sort } from 'app/shared/backend-models/interfaces';

const configServiceStub = {
  getRestServiceRoot: jasmine
    .createSpy('getRestServiceRoot')
    .and.returnValue(of('http://localhost:8081/mythaistar/services/rest/')),
};

const httpClientStub = {
  patch: jasmine.createSpy('patch').and.returnValue(of({})),
  post: jasmine.createSpy('post').and.returnValue(of({})),
};

const translocoServiceStub = {
  selectTranslateObject: jasmine
    .createSpy('selectTranslateObject')
    .and.returnValue(
      of({
        recorded: 'Recorded',
        cooking: 'Preparing',
        ready: 'Ready',
        handingover: 'Handing Over',
        delivered: 'Delivered',
        completed: 'Completed',
        canceled: 'Canceled',

        pending: 'Pending',
        payed: 'Payed',
        refunded: 'Refunded',
      }),
    ),
};

const orderData: OrderView[] = [
  {
    dish: {
      id: 0,
      name: 'dish1',
      price: 14.75,
    },
    extras: [
      { id: 0, price: 1, name: 'Extra Curry' },
      { id: 1, price: 2, name: 'Extra pork' },
    ],
    orderLine: {
      amount: 2,
      comment: 'comment',
    },
  },
  {
    dish: {
      id: 0,
      name: 'dish2',
      price: 12.15,
    },
    extras: [{ id: 0, price: 1, name: 'Extra Curry' }],
    orderLine: {
      amount: 1,
      comment: '',
    },
  },
];

describe('WaiterCockpitService', () => {
  let initialState;
  let configService: ConfigService;
  let httpClient: HttpClient;
  let priceCalcualtorService: PriceCalculatorService;
  let service: WaiterCockpitService;
  let translocoService: TranslocoService;

  beforeEach(async(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({ initialState }),
        { provide: ConfigService, useValue: configServiceStub },
        { provide: HttpClient, useValue: httpClientStub },
        { provide: TranslocoService, useValue: translocoServiceStub },
        PriceCalculatorService,
        WaiterCockpitService,
      ],
      imports: [CoreModule],
    });
    service = TestBed.inject(WaiterCockpitService);
    configService = TestBed.inject(ConfigService);
    httpClient = TestBed.inject(HttpClient);
    priceCalcualtorService = TestBed.inject(PriceCalculatorService);
    translocoService = TestBed.inject(TranslocoService);
  }));

  it('should create', async(() => {
    expect(service).toBeTruthy();
  }));

  it('should set order status', async(() => {
    service.setOrderStatus(0, 1).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.patch).toHaveBeenCalled();
  }));

  it('should set payment status', async(() => {
    service.setPaymentStatus(0, 1).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.patch).toHaveBeenCalled();
  }));

  it('should update order status translations', async(() => {
    service.updateOrderStatusTranslation(translocoService, 'en');
    expect(translocoService.selectTranslateObject).toHaveBeenCalled();
    expect(service.orderStatusTranslation).toEqual([
      { name: 'orderStatus.recorded', label: 'Recorded' },
      { name: 'orderStatus.cooking', label: 'Preparing' },
      { name: 'orderStatus.ready', label: 'Ready' },
      { name: 'orderStatus.handingover', label: 'Handing Over' },
      { name: 'orderStatus.delivered', label: 'Delivered' },
      { name: 'orderStatus.completed', label: 'Completed' },
      { name: 'orderStatus.canceled', label: 'Canceled' },
    ]);
  }));

  it('should get reservations', async(() => {
    const pageable: Pageable = {
      pageSize: 8,
      pageNumber: 0,
    };
    const sorting: Sort[] = [
      { property: undefined, direction: undefined},
    ];
    const filters: FilterCockpit = {
      bookingDate: undefined,
      bookingToken: undefined,
      bookingType: undefined,
      email: undefined,
      name: undefined,
      orderstatus: undefined,
      paymentstatus: undefined,
      table: undefined,
    };
    service.getReservations(pageable, sorting, filters).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.post).toHaveBeenCalled();
  }));

  it('should get orders', async(() => {
    const pageable: Pageable = {
      pageSize: 8,
      pageNumber: 0,
    };
    const sorting: Sort[] = [
      { property: undefined, direction: undefined},
    ];
    const filters: FilterCockpit = {
      bookingDate: undefined,
      bookingToken: undefined,
      bookingType: undefined,
      email: undefined,
      name: undefined,
      orderstatus: undefined,
      paymentstatus: undefined,
      table: undefined,
    };
    service.getOrders(pageable, sorting, filters).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.post).toHaveBeenCalled();
  }));

  it('should get correct price total', async(() => {
    expect(service.getTotalPrice(orderData)).toEqual(((14.75 + 1 + 2) * 2) + 12.15 + 1);
  }));

  it('should compose correct order info', async(() => {
    const orderResult: any = [
      {
        dish: { id: 0, name: 'dish1', price: (14.75 + 1 + 2) * 2 },
        extras: 'Extra Curry, Extra pork',
        orderLine: { amount: 2, comment: 'comment' },
      },
      {
        dish: { id: 0, name: 'dish2', price: 12.15 + 1 },
        extras: 'Extra Curry',
        orderLine: { amount: 1, comment: '' },
      },
    ]; // 2 dishes + 1 extra curry + 2 extra pork // 1 extra curry

    expect(service.orderComposer(orderData)).toEqual(orderResult);
  }));
});
