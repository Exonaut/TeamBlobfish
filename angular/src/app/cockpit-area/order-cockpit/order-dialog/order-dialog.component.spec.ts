import { async, TestBed, ComponentFixture, tick, fakeAsync } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from '../../../core/core.module';
import { PriceCalculatorService } from '../../../sidenav/services/price-calculator.service';
import { WaiterCockpitModule } from '../../cockpit.module';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { OrderDialogComponent } from './order-dialog.component';
import { ConfigService } from '../../../core/config/config.service';
import { provideMockStore } from '@ngrx/store/testing';
import { config } from '../../../core/config/config';
import { getTranslocoModule } from '../../../transloco-testing.module';
import { dialogOrderDetails } from '../../../../in-memory-test-data/db-order-dialog-data';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from 'app/shared/common/test-utils';
import { of } from 'rxjs/internal/observable/of';
import { TranslocoService } from '@ngneat/transloco';
import { dialog } from 'electron';



const translocoServiceStub = {
  selectTranslateObject: of({
    creationDateH: 'Creation Date',
    reservationDateH: 'Reservation Date',
    ownerH: 'Owner',
    emailH: 'Email',
    tableH: 'Table ID',
    bookingStateH: 'State',
    paymentStateH: 'Payment',
    dishH: 'Dish',
    commentsH: 'Comments',
    extrasH: 'Extras',
    quantityH: 'Amount',
    priceH: 'Price',
    recorded: 'Recorded',
    cooking: 'Cooking',
    ready: 'Ready',
    handingover: 'Handing Over',
    delivered: 'Delivered',
    completed: 'Completed',
    canceled: 'Canceled',
    pending: 'Pending',
    payed: 'Payed',
    refunded: 'Refunded',
  } as any),
};

const waiterCockpitServiceStub = {
  getTotalPrice: jasmine.createSpy('getTotalPrice').and.returnValue(
    100
  ),
  orderComposer: jasmine.createSpy('orderComposer').and.returnValue(
    [{}]
  ),
  setOrderStatus: jasmine.createSpy('setOrderStatus').and.returnValue(
    of({})
  ),
  setPaymentStatus: jasmine.createSpy('setPaymentStatus').and.returnValue(
    of({})
  ),
};

const matDialogRefStub = {
  close: jasmine.createSpy('close')
};

describe('OrderDialogComponent', () => {
  let component: OrderDialogComponent;
  let fixture: ComponentFixture<OrderDialogComponent>;
  let initialState;
  let el: DebugElement;
  let translocoService: TranslocoService;

  beforeEach(async(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      declarations: [OrderDialogComponent],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: dialogOrderDetails },
        { provide: WaiterCockpitService, useValue: waiterCockpitServiceStub },
        { provide: MatDialogRef, useValue: matDialogRefStub },
        TranslocoService,
        provideMockStore({ initialState }),
        ConfigService
      ],
      imports: [
        BrowserAnimationsModule,
        WaiterCockpitModule,
        getTranslocoModule(),
        CoreModule,
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(OrderDialogComponent);
      component = fixture.componentInstance;
      el = fixture.debugElement;
      fixture.detectChanges();
      translocoService = TestBed.inject(TranslocoService);
      spyOn(translocoService, 'selectTranslateObject').and.returnValue(
        translocoServiceStub.selectTranslateObject
      );
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should verify table headers', () => {
    expect(component.columnst[0].label === 'Reservation Date').toBeTruthy();
    expect(component.columnst[1].label === 'Creation Date').toBeTruthy();
    expect(component.columnst[2].label === 'Owner').toBeTruthy();
    expect(component.columnst[3].label === 'Email').toBeTruthy();
    expect(component.columnst[4].label === 'Table ID').toBeTruthy();
    expect(component.columnst[5].label === 'State').toBeTruthy();
    expect(component.columnst[6].label === 'Payment').toBeTruthy();

    expect(component.columnso[0].label === 'Dish').toBeTruthy();
    expect(component.columnso[1].label === 'Comments').toBeTruthy();
    expect(component.columnso[2].label === 'Extras').toBeTruthy();
    expect(component.columnso[3].label === 'Amount').toBeTruthy();
    expect(component.columnso[4].label === 'Price').toBeTruthy();
  });

  it('should verify status names', () => {
    expect(component.statusNamesMap[0] === 'Recorded').toBeTruthy();
    expect(component.statusNamesMap[1] === 'Cooking').toBeTruthy();
    expect(component.statusNamesMap[2] === 'Ready').toBeTruthy();
    expect(component.statusNamesMap[3] === 'Handing Over').toBeTruthy();
    expect(component.statusNamesMap[4] === 'Delivered').toBeTruthy();
    expect(component.statusNamesMap[5] === 'Completed').toBeTruthy();
    expect(component.statusNamesMap[6] === 'Canceled').toBeTruthy();

    expect(component.paymentNamesMap[0] === 'Pending').toBeTruthy();
    expect(component.paymentNamesMap[1] === 'Payed').toBeTruthy();
    expect(component.paymentNamesMap[2] === 'Refunded').toBeTruthy();
  });

  it('should call the service when applying changes and close the dialog', () => {
    component.applyChanges();
    expect(waiterCockpitServiceStub.setOrderStatus).toHaveBeenCalled();
    expect(waiterCockpitServiceStub.setPaymentStatus).toHaveBeenCalled();
    expect(matDialogRefStub.close).toHaveBeenCalled();
  });

  // it('should apply changes on submit', fakeAsync(() => {
  //   spyOn(component, 'applyChanges');
  //   const submitButton = el.query(By.css('.submitButton')).nativeElement;
  //   click(submitButton);
  //   tick();
  //   fixture.detectChanges();
  //   expect(component.applyChanges).toHaveBeenCalled();
  // }));

  // it('should display correct tableData', () => {
  //   const bookingDate = el.query(By.css('.bookingDateData'));
  //   const creationDate = el.query(By.css('.creationDateData'));
  //   const name = el.query(By.css('.nameData'));
  //   const email = el.query(By.css('.emailData'));
  //   const tableId = el.query(By.css('.tableIdData'));
  //   const orderStatus = el.query(By.css('.statusData'));
  //   const paymentStatus = el.query(By.css('.paymentData'));

  //   expect(bookingDate.nativeElement.textContent.trim()).toBe('');
  //   expect(creationDate.nativeElement.textContent.trim()).toBe('');
  //   expect(name.nativeElement.textContent.trim()).toBe('user0');
  //   expect(email.nativeElement.textContent.trim()).toBe('user0@mail.com');
  //   expect(tableId.nativeElement.textContent.trim()).toBe('');
  //   expect(orderStatus.nativeElement.textContent.trim()).toBe('');
  //   expect(paymentStatus.nativeElement.textContent.trim()).toBe('');
  // });
});
