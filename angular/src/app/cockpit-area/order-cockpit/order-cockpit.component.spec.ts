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
import {
  orderData,
  orderDataResponse,
} from '../../../in-memory-test-data/db-order';
import { ActivatedRoute } from '@angular/router';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { ConfirmDialogComponent } from 'app/shared/confirm-dialog/confirm-dialog.component';
import { FilterCockpit } from 'app/shared/backend-models/interfaces';

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
    actionsH: 'Actions',
    serveTimeH: 'Serving Time',
    tableIdH: 'Table ID',
  } as any),
};

const waiterCockpitServiceStub = {
  getOrders: jasmine
    .createSpy('getOrders')
    .and.returnValue(of(orderDataResponse)),
  updateOrderStatusTranslation: jasmine
    .createSpy('updateOrderStatusTranslation')
    .and.returnValue(of({}).subscribe()),
  updatePaymentStatusTranslation: jasmine
    .createSpy('updatePaymentStatusTranslation')
    .and.returnValue(of({}).subscribe()),
  orderStatusTranslation: [
    'Recorded',
    'Cooking',
    'Ready',
    'Handing Over',
    'Delivered',
    'Completed',
    'Canceled',
  ],
  paymentStatusTranslation: ['Pending', 'Payed', 'Refunded'],
  setOrderStatus: jasmine.createSpy('setOrderStatus').and.returnValue(of({})),
  setPaymentStatus: jasmine
    .createSpy('setPaymentStatus')
    .and.returnValue(of({})),
};

const waiterCockpitServiceSortStub = {
  getOrders: jasmine.createSpy('getOrders').and.returnValue(of(ascSortOrder)),
  updateOrderStatusTranslation: jasmine
    .createSpy('updateOrderStatusTranslation')
    .and.returnValue(of({}).subscribe()),
  updatePaymentStatusTranslation: jasmine
    .createSpy('updatePaymentStatusTranslation')
    .and.returnValue(of({}).subscribe()),
  orderStatusTranslation: [
    'Recorded',
    'Cooking',
    'Ready',
    'Handing Over',
    'Delivered',
    'Completed',
    'Canceled',
  ],
  paymentStatusTranslation: ['Pending', 'Payed', 'Refunded'],
  setOrderStatus: jasmine.createSpy('setOrderStatus').and.returnValue(of({})),
  setPaymentStatus: jasmine
    .createSpy('setPaymentStatus')
    .and.returnValue(of({})),
};

const activatedRouteStub = {
  data: of({ archive: false }),
};

class TestBedSetUp {
  static loadWaiterCockpitServiceStud(waiterCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [OrderCockpitComponent],
      providers: [
        { provide: MatDialog, useValue: mockDialog },
        { provide: WaiterCockpitService, useValue: waiterCockpitStub },
        { provie: ActivatedRoute, useValue: activatedRouteStub },
        SnackBarService,
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
          translocoServiceStub.selectTranslateObject,
        );
      });
  }));

  // beforeEach(() => {
  //   fixture = TestBed.createComponent(OrderCockpitComponent);
  //   component = fixture.componentInstance;
  // });

  it('should create component', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should verify table header names', () => {
    fixture.detectChanges();
    expect(component.columns[0].label === 'Reservation Date').toBeTruthy();
    expect(component.columns[1].label === 'Name').toBeTruthy();
    expect(component.columns[2].label === 'Email').toBeTruthy();
    expect(component.columns[3].label === 'Reference Number').toBeTruthy();
    expect(component.columns[4].label === 'Serving Time').toBeTruthy();
    expect(component.columns[5].label === 'Status').toBeTruthy();
    expect(component.columns[6].label === 'Payment').toBeTruthy();
    expect(component.columns[7].label === 'Actions').toBeTruthy();
    expect(component.columns[8].label === 'Table ID').toBeTruthy();
  });

  it('should verify content and total records of orders', fakeAsync(() => {
    fixture.detectChanges();
    tick();
    expect(component.orders).toEqual(orderData);
    expect(component.totalOrders).toBe(orderDataResponse.totalElements);
  }));

  it('should go to next page of orders', () => {
    fixture.detectChanges();
    component.page({
      pageSize: 100,
      pageIndex: 2,
      length: 50,
    });
    expect(component.orders).toEqual(orderData);
    expect(component.totalOrders).toBe(orderDataResponse.totalElements);
  });

  it('should clear form and reset', fakeAsync(() => {
    fixture.detectChanges();
    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);
    fixture.detectChanges();
    tick();
    expect(component.orders).toEqual(orderData);
    expect(component.totalOrders).toBe(orderDataResponse.totalElements);
  }));

  it('should filter the order table on click of submit', fakeAsync(() => {
    spyOn(component, 'applyFilters').and.callThrough();
    fixture.detectChanges();
    const submit = el.query(By.css('.orderApplyFilters'));
    click(submit);
    tick();
    expect(component.orders).toEqual(orderData);
    expect(component.totalOrders).toBe(orderDataResponse.totalElements);
    expect(component.applyFilters).toHaveBeenCalled();
  }));

  it('should clear filter on click', fakeAsync(() => {
    spyOn(component, 'clearFilters').and.callThrough();
    fixture.detectChanges();
    component.filters = {
      bookingDate: 'Date',
      email: 'Email',
      bookingToken: 'Token',
      orderstatus: [0],
      paymentstatus: [0],
      name: 'Name',
      table: 3,
      bookingType: 0,
    } as FilterCockpit;
    const btn = el.query(By.css('.orderClearFilters'));
    click(btn);
    console.log(component.filters);
    expect(component.clearFilters).toHaveBeenCalled();
    expect(
      JSON.stringify(component.filters) ===
        JSON.stringify({
          bookingDate: undefined,
          email: undefined,
          bookingToken: undefined,
          orderstatus: [0, 1, 2, 3, 4],
          paymentstatus: [0, 1],
          name: undefined,
          table: undefined,
          bookingType: undefined,
        }),
    ).toBeTruthy();
  }));

  it('should open order dialog on click of row', fakeAsync(() => {
    spyOn(component, 'selected').and.callThrough();
    component.applyFilters();
    fixture.detectChanges();
    tick();
    const row = el.query(By.css('.mat-row'));
    click(row, {
      target: {
        className: {
          includes(): boolean {
            return false;
          },
        },
      },
    });
    expect(component.selected).toHaveBeenCalled();
    expect(dialog.open).toHaveBeenCalled();
  }));

  it('should show action buttons', () => {
    fixture.detectChanges();
    expect(el.query(By.css('.advanceOrder'))).toBeTruthy();
    expect(el.query(By.css('.switchPayment'))).toBeTruthy();
    expect(el.query(By.css('.cancelOrder'))).toBeTruthy();
  });

  it('should advance order on click', () => {
    spyOn(component, 'applyChanges').and.callThrough();
    fixture.detectChanges();
    const btn = el.query(By.css('.advanceOrder'));
    expect(btn).toBeTruthy();
    click(btn);
    expect(component.applyChanges).toHaveBeenCalledWith(orderData[0], 1, null);
    expect(waiterCockpitService.setOrderStatus).toHaveBeenCalled();
    expect(component.undoValues.length === 1);
  });

  it('should change payment status on', () => {
    spyOn(component, 'applyChanges').and.callThrough();
    fixture.detectChanges();
    const btn = el.query(By.css('.switchPayment'));
    expect(btn).toBeTruthy();
    click(btn);
    expect(component.applyChanges).toHaveBeenCalledWith(orderData[0], null, 1);
    expect(waiterCockpitService.setPaymentStatus).toHaveBeenCalled();
    expect(component.undoValues.length === 1).toBeTruthy();
  });

  it('should cancel order on click', () => {
    spyOn(component, 'applyChanges').and.callThrough();
    fixture.detectChanges();
    const btn = el.query(By.css('.cancelOrder'));
    expect(btn).toBeTruthy();
    click(btn);
    expect(component.applyChanges).toHaveBeenCalledWith(orderData[0], 6, 0);
    expect(waiterCockpitService.setOrderStatus).toHaveBeenCalled();
    expect(waiterCockpitService.setPaymentStatus).toHaveBeenCalled();
    expect(component.undoValues.length === 1).toBeTruthy();
  });

  it('should revert changes on click', () => {
    spyOn(component, 'undoLastChange').and.callThrough();
    fixture.detectChanges();
    component.applyChanges(orderData[0], 1, 1);
    expect(component.undoValues.length === 1).toBeTruthy();
    fixture.detectChanges();
    const btn = el.query(By.css('.undoButton'));
    expect(btn).toBeTruthy();
    click(btn);
    expect(component.undoLastChange).toHaveBeenCalled();
    expect(waiterCockpitService.setOrderStatus).toHaveBeenCalledWith(
      orderData[0].order.id,
      orderData[0].order.orderStatus,
    );
    expect(waiterCockpitService.setPaymentStatus).toHaveBeenCalledWith(
      orderData[0].order.id,
      orderData[0].order.paymentStatus,
    );
    expect(component.undoValues.length === 0).toBeTruthy();
  });

  it('should put history object on undo stack', () => {
    component.applyChanges(orderData[0], 1, 1);
    expect(component.undoValues.length === 1).toBeTruthy();
    expect(component.undoValues[0].id === orderData[0].order.id).toBeTruthy();
    expect(
      component.undoValues[0].orderStatus === orderData[0].order.orderStatus,
    ).toBeTruthy();
    expect(
      component.undoValues[0].paymentStatus ===
        orderData[0].order.paymentStatus,
    ).toBeTruthy();
  });
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
