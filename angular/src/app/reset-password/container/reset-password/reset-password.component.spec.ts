import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { config } from '../../../core/config/config';
import { ResetPasswordComponent } from './reset-password.component';
import { ConfigService } from '../../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { SnackBarService } from '../../../../app/core/snack-bar/snack-bar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from '../../../core/core.module';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { getTranslocoModule } from '../../../transloco-testing.module';
import { ResetPasswordService } from 'app/reset-password/services/reset-password.service';
import { DebugElement } from '@angular/core';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { click } from 'app/shared/common/test-utils';

const routerStub = {
  navigate: jasmine.createSpy('naviagte')
};

const activatedRouteStub = {
  paramMap: of({
    get: jasmine.createSpy('get').and.returnValue('Token')
  })
};

const resetPasswordServiceStub = {
  resetPassword: jasmine.createSpy('resetPassword').and.returnValue(
    of({})
  )
};

const snackBarServiceStub = {
  openSnack: jasmine.createSpy('resetPassword')
};

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;
  let mockStore: MockStore;
  let el: DebugElement;
  let resetPasswordService: ResetPasswordService;
  let snackBarService: SnackBarService;
  let router: Router;
  let activatedRoute: ActivatedRoute;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ResetPasswordComponent],
      providers: [
        { provide: ResetPasswordService, useValue: resetPasswordServiceStub },
        { provide: SnackBarService, useValue: snackBarServiceStub },
        { provide: Router, useValue: routerStub },
        { provide: ActivatedRoute, useValue: activatedRouteStub },
        TranslocoService,
        provideMockStore(),
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
    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    mockStore = TestBed.inject(MockStore);
    snackBarService = TestBed.inject(SnackBarService);
    resetPasswordService = TestBed.inject(ResetPasswordService);
    router = TestBed.inject(Router);
    activatedRoute = TestBed.inject(ActivatedRoute);
    el = fixture.debugElement;
    fixture.detectChanges();
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should send reset password request and navigate to home on submit', () => {
    const fakeEvent = { preventDefault: () => {} };
    const form = el.query(By.css('form'));
    form.triggerEventHandler('submit', fakeEvent);
    expect(resetPasswordService.resetPassword).toHaveBeenCalled();
    expect(snackBarService.openSnack).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalled();
  });

});
