import { async, TestBed, ComponentFixture, tick } from '@angular/core/testing';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
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



// const translocoServiceStub = {
//   selectTranslateObject: of({
//     creationDate: 'Reservation Date',
//     name: 'Email',
//     email: 'Reference Number',
//     tableId: 'Owner',
//     orderStatus: 'Table',
//     paymentStatus: 'Creation date',
//   } as any),
// };


describe('OrderDialogComponent', () => {
  let component: OrderDialogComponent;
  let fixture: ComponentFixture<OrderDialogComponent>;
  let initialState;
  let el: DebugElement;
  let translocoService: TranslocoService;

  beforeEach(async(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: dialogOrderDetails },
        WaiterCockpitService,
        PriceCalculatorService,
        TranslocoService,
        provideMockStore({ initialState }),
        ConfigService
      ],
      imports: [
        BrowserAnimationsModule,
        WaiterCockpitModule,
        getTranslocoModule(),
        CoreModule
      ],
    }).compileComponents().then(() => {
      fixture = TestBed.createComponent(OrderDialogComponent);
      component = fixture.componentInstance;
      el = fixture.debugElement;
      fixture.detectChanges();
      translocoService = TestBed.inject(TranslocoService);
    });
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // it('should display Status: Recorded', () => {

  // });

  // it('should display Payment: Pending', () => {

  // });

  it('should close order-dialog on click of submit', () => {
    fixture.detectChanges();
    const submit = el.nativeElement.queryAll(By.css('.submitButton'));
    click(submit);
    tick();
    expect(component.applyChanges).toHaveBeenCalled();
  });

  it('should reset inputs on click of cancel', () => {
    const cancel = el.query(By.css('.cancelButton'));
    click(cancel);
    fixture.detectChanges();
    tick();
    expect(component.applyChanges).toHaveBeenCalled();
  });

  it('should display correct tableData', () => {
    const bookingDate = el.query(By.css('.bookingDateData'));
    const creationDate = el.query(By.css('.creationDateData'));
    const name = el.query(By.css('.nameData'));
    const email = el.query(By.css('.emailData'));
    const tableId = el.query(By.css('.tableIdData'));
    const orderStatus = el.query(By.css('.statusData'));
    const paymentStatus = el.query(By.css('.paymentData'));

    // expect(bookingDate.nativeElement.textContent.trim()).toBe('');
    // expect(creationDate.nativeElement.textContent.trim()).toBe('');
    // expect(name.nativeElement.textContent.trim()).toBe('user0');
    // expect(email.nativeElement.textContent.trim()).toBe('user0@mail.com');
    // expect(tableId.nativeElement.textContent.trim()).toBe('');
    // expect(orderStatus.nativeElement.textContent.trim()).toBe('');
    // expect(paymentStatus.nativeElement.textContent.trim()).toBe('');
  });
});
