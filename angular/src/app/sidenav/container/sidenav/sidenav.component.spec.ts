import { NO_ERRORS_SCHEMA, DebugElement, ChangeDetectorRef } from '@angular/core';
import {
  async,
  ComponentFixture,
  TestBed,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { Store } from '@ngrx/store';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { SidenavService } from '../../services/sidenav.service';
import { PriceCalculatorService } from '../../services/price-calculator.service';
import { CoreModule } from '../../../core/core.module';
import { SnackBarService } from '../../../core/snack-bar/snack-bar.service';
import * as fromOrder from '../../store';
import { SidenavComponent } from './sidenav.component';
import { getTranslocoModule } from '../../../transloco-testing.module';
import { getAllOrderData } from '../../../../in-memory-test-data/db-order-data';
import * as fromSideNavState from '../../../sidenav/store/reducers';
import { SidenavOrderComponent } from '../../components/sidenav-order/sidenav-order.component';
import { By } from '@angular/platform-browser';
import { click } from '../../../shared/common/test-utils';
import { MemoizedSelector } from '@ngrx/store';
import { Order } from '../../models/order.model';
import { MatDialog } from '@angular/material/dialog';
import { of } from 'rxjs/internal/observable/of';
import { TranslocoService } from '@ngneat/transloco';
import { ConfigService } from 'app/core/config/config.service';
import { ReactiveFormsModule } from '@angular/forms';

const mockDialog = {
  open: jasmine.createSpy('open').and.returnValue({
    afterClosed: () => of('content'),
  }),
};

const sideNavServiceStub = {
  closeSideNav: jasmine.createSpy('closeSideNav'),
  postBooking: jasmine.createSpy('postBooking').and.returnValue(
    of({
      bookingToken: 'BT'
    })
  ),
  sendOrders: jasmine.createSpy('sendOrders').and.returnValue(
    of({})
  ),
};

const snackBarServiceStub = {
  openSnack: jasmine.createSpy('openSnack'),
};

const changeDetectorRefStub = {

};

describe('SidenavComponent', () => {
  let component: SidenavComponent;
  let fixture: ComponentFixture<SidenavComponent>;
  let mockStore: MockStore;
  let el: DebugElement;
  let mockOrdersSelector: MemoizedSelector<fromOrder.SideNavState, Order[]>;
  let dialog: MatDialog;
  let sidenavService: SidenavService;
  let snackBarService: SnackBarService;

  const STATE = {
    sidenav: {
      order: getAllOrderData,
      selectedOrderId: null,
    },
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      schemas: [NO_ERRORS_SCHEMA],
      declarations: [SidenavComponent, SidenavOrderComponent],
      providers: [
        PriceCalculatorService,
        { provide: SidenavService, useValue: sideNavServiceStub },
        { provide: SnackBarService, useValue: snackBarServiceStub },
        { provide: ChangeDetectorRef, useValue: changeDetectorRefStub },
        TranslocoService,
        provideMockStore(),
        { provide: MatDialog, useValue: mockDialog }
      ],
      imports: [
        BrowserAnimationsModule,
        getTranslocoModule(),
        RouterTestingModule,
        ReactiveFormsModule,
        CoreModule,
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavComponent);
    component = fixture.componentInstance;
    mockStore = TestBed.inject(MockStore);
    mockOrdersSelector = mockStore.overrideSelector(
      fromOrder.getAllOrders,
      getAllOrderData,
    );
    snackBarService = TestBed.inject(SnackBarService);
    sidenavService = TestBed.inject(SidenavService);
    el = fixture.debugElement;
    dialog = TestBed.inject(MatDialog);
    fixture.detectChanges();
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update or increased the order by clicking + operator', () => {
    spyOn(mockStore, 'dispatch').and.callThrough();
    const btn = el.queryAll(By.css('.increaseOrder'));
    click(btn[0]);
    expect(mockStore.dispatch).toHaveBeenCalled();
  });

  it('should update or decrease the order by clicking - operator', () => {
    spyOn(mockStore, 'dispatch').and.callThrough();
    const btn = el.queryAll(By.css('.decreaseOrder'));
    click(btn[0]);
    expect(mockStore.dispatch).toHaveBeenCalled();
  });

  it('should remove the order by clicking the close icon [X]', () => {
    spyOn(mockStore, 'dispatch').and.callThrough();
    const btn = el.queryAll(By.css('.removeOrderBtn'));
    click(btn[0]);
    expect(mockStore.dispatch).toHaveBeenCalled();
  });

  it('should remove the comment on click of Remove Comment text', () => {
    spyOn(mockStore, 'dispatch').and.callThrough();
    const btn = el.queryAll(By.css('.removeOrderComment'));
    click(btn[0]);
    expect(mockStore.dispatch).toHaveBeenCalled();
  });

  it('should add the comment on click of Add Comment text', fakeAsync(() => {
    spyOn(mockStore, 'dispatch').and.callThrough();
    const btn = el.queryAll(By.css('.addOrderComment'));
    click(btn[0]);
    fixture.detectChanges();
    tick();
    expect(dialog.open).toHaveBeenCalled();
    expect(mockStore.dispatch).toHaveBeenCalled();
  }));

  it('should send the order details to the end user on click of ID send button', () => {
    spyOn(component, 'sendOrder').and.callThrough();
    const btn = el.query(By.css('.orderSubmitId'));
    click(btn);
    expect(sidenavService.sendOrders).toHaveBeenCalled();
    expect(component.sendOrder).toHaveBeenCalled();
  });

  it('should send the booking and order on click of booking send button', async(() => {
    spyOn(component, 'sendBooking').and.callThrough();
    component.selectedTab = 1;
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      const btn = el.nativeElement.querySelector('.orderSubmit');
      console.log(btn);
      click(btn);
      expect(component.sendBooking).toHaveBeenCalled();
      expect(sidenavService.postBooking).toHaveBeenCalled();
      expect(sidenavService.sendOrders).toHaveBeenCalled();
      expect(snackBarService.openSnack).toHaveBeenCalled();
    });
    expect(true).toBeTruthy();
  }));
});
