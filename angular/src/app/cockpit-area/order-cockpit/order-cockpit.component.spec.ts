import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { ConfigService } from '../../core/config/config.service';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { OrderCockpitComponent } from './order-cockpit.component';
import { config } from '../../core/config/config';
import {
  TestBed,
  ComponentFixture,
  async,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslocoService } from '@ngneat/transloco';
import { of } from 'rxjs/internal/observable/of';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getTranslocoModule } from '../../transloco-testing.module';
import { CoreModule } from '../../core/core.module';
import { PageEvent } from '@angular/material/paginator';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from '../../shared/common/test-utils';
import { ascSortOrder } from '../../../in-memory-test-data/db-order-asc-sort';
import { orderData } from '../../../in-memory-test-data/db-order';
import { ActivatedRoute } from '@angular/router';

const mockDialog = {
  open: jasmine.createSpy('open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

const translocoServiceStub = {
  selectTranslateObject: of({
    reservationDateH: 'Reservation Date',
    emailH: 'Email',
    bookingTokenH: 'Reference Number',
    ownerH: 'Owner',
    tableH: 'Table',
    creationDateH: 'Creation date',
    bookingStateH: 'Status',
    paymentStateH: 'Payment',
    nameH: 'Name',
    actionsH: 'Actions'
  } as any),
};

const waiterCockpitServiceStub = {
  getOrders: jasmine.createSpy('getOrders').and.returnValue(of(orderData)),
  updateOrderStatusTranslation: jasmine.createSpy('updateOrderStatusTranslation').and.returnValue(of({}).subscribe()),
  updatePaymentStatusTranslation: jasmine.createSpy('updatePaymentStatusTranslation').and.returnValue(of({}).subscribe()),
  orderStatusTranslation: [
    'Recorded',
    'Cooking',
    'Ready',
    'Handing Over',
    'Delivered',
    'Completed',
    'Canceled'
  ],
  paymentStatusTranslation: [
    'Pending',
    'Payed',
    'Refunded'
  ]
};

const waiterCockpitServiceSortStub = {
  getOrders: jasmine.createSpy('getOrders').and.returnValue(of(ascSortOrder)),
  updateOrderStatusTranslation: jasmine.createSpy('updateOrderStatusTranslation').and.returnValue(of({}).subscribe()),
  updatePaymentStatusTranslation: jasmine.createSpy('updatePaymentStatusTranslation').and.returnValue(of({}).subscribe()),
  orderStatusTranslation: [
    'Recorded',
    'Cooking',
    'Ready',
    'Handing Over',
    'Delivered',
    'Completed',
    'Canceled'
  ],
  paymentStatusTranslation: [
    'Pending',
    'Payed',
    'Refunded'
  ]
};

const activatedRouteStub = {
  data: of({archive: false})
};

class TestBedSetUp {
  static loadWaiterCockpitServiceStud(waiterCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [OrderCockpitComponent],
      providers: [
        { provide: MatDialog, useValue: mockDialog },
        { provide: WaiterCockpitService, useValue: waiterCockpitStub },
        { provie: ActivatedRoute, useValue: activatedRouteStub},
        TranslocoService,
        ConfigService,
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
        RouterTestingModule,
      ],
    });
  }
}

describe('OrderCockpitComponent', () => {
  let component: OrderCockpitComponent;
  let fixture: ComponentFixture<OrderCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let waiterCockpitService: WaiterCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadWaiterCockpitServiceStud(waiterCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(OrderCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
        spyOn(translocoService, 'selectTranslateObject').and.returnValue(
          translocoServiceStub.selectTranslateObject
        );
      });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderCockpitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should verify table header names', () => {
    expect(component.columns[0].label === 'Reservation Date').toBeTruthy();
    expect(component.columns[1].label === 'Name').toBeTruthy();
    expect(component.columns[2].label === 'Email').toBeTruthy();
    expect(component.columns[3].label === 'Reference Number').toBeTruthy();
    expect(component.columns[4].label === 'Status').toBeTruthy();
    expect(component.columns[5].label === 'Payment').toBeTruthy();
    expect(component.columns[6].label === 'Actions').toBeTruthy();
  });

  it('should verify content and total records of orders', fakeAsync(() => {
    fixture.detectChanges();
    tick();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  }));

  it('should go to next page of orders', () => {
    component.page({
      pageSize: 100,
      pageIndex: 2,
      length: 50,
    });
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  });

  it('should clear form and reset', fakeAsync(() => {
    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);
    fixture.detectChanges();
    tick();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  }));

  // it('should open OrderDialogComponent dialog on click of row', fakeAsync(() => {
  //   const rows = el.queryAll(By.css('.mat-row'));
  //   console.warn(rows);
  //   click(rows[0]);
  //   fixture.detectChanges();
  //   tick();
  //   expect(dialog.open).toHaveBeenCalled();
  // }));

  it('should filter the order table on click of submit', fakeAsync(() => {
    fixture.detectChanges();
    const submit = el.query(By.css('.orderApplyFilters'));
    click(submit);
    tick();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  }));

});

describe('TestingOrderCockpitComponentWithSortOrderData', () => {
  let component: OrderCockpitComponent;
  let fixture: ComponentFixture<OrderCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let waiterCockpitService: WaiterCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadWaiterCockpitServiceStud(waiterCockpitServiceSortStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(OrderCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should sort records of orders', () => {
    component.sort({
      active: 'Reservation Date',
      direction: 'asc',
    });
    expect(component.orders).toEqual(ascSortOrder.content);
    expect(component.totalOrders).toBe(8);
  });
});
