import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { of } from 'rxjs/internal/observable/of';
import { ChangePasswordDialogComponent } from './change-password-dialog.component';
import { config } from '../../../../core/config/config';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../../../store';
import { ConfigService } from '../../../../core/config/config.service';
import { UserCockpitService } from '../../../services/user-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getTranslocoModule } from '../../../../transloco-testing.module';
import { CoreModule } from '../../../../core/core.module';
import { GetAllUsersData } from '../../../../../in-memory-test-data/db-users';
import { DebugElement } from '@angular/core';
import { provideMockStore } from '@ngrx/store/testing';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';

const translocoServiceStub = {
  selectTranslateObject: of({

  } as any),
};

const userCockpitServiceStub = {
  getUsers: jasmine.createSpy('getUsers').and.returnValue(
    of({content: GetAllUsersData}),
  ),
  changePassword: jasmine.createSpy('changePassword').and.returnValue(
    of({})
  )
};

const matDialogRefStub = {
  close: jasmine.createSpy('close'),
};

const formValueStub = {
  password: 'Password',
};

const snackBarServiceStub = {
  openSnack: jasmine.createSpy('openSnack'),
};

class TestBedSetUp {
  static loadUserCockpitServiceStud(userCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [ChangePasswordDialogComponent],
      providers: [
        { provide: UserCockpitService, useValue: userCockpitStub },
        { provide: MAT_DIALOG_DATA, useValue: GetAllUsersData[0]},
        { provide: MatDialogRef, useValue: matDialogRefStub},
        { provide: SnackBarService, useValue: snackBarServiceStub},
        TranslocoService,
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
      ],
    });
  }
}

describe('UserDialogComponent', () => {
  let component: ChangePasswordDialogComponent;
  let fixture: ComponentFixture<ChangePasswordDialogComponent>;
  let store: Store<State>;
  let initialState;
  let userCockpitService: UserCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadUserCockpitServiceStud(userCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(ChangePasswordDialogComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        userCockpitService = TestBed.inject(UserCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChangePasswordDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component of ChangePasswordDialog', fakeAsync(() => {
    spyOn(translocoService, 'selectTranslateObject').and.returnValue(
      translocoServiceStub.selectTranslateObject,
    );
    fixture.detectChanges();
    tick();
    expect(component).toBeTruthy();
  }));

  it('should verify content of ChangePasswordDialog', () => {
    expect(component.data).toEqual(GetAllUsersData[0]);
  });

  it('should change the Password', () => {
    spyOn(component, 'closeWithRefresh').and.callThrough();

    component.changePassword(formValueStub);

    expect(userCockpitServiceStub.changePassword).toHaveBeenCalled();
    expect(component.closeWithRefresh).toHaveBeenCalled();
    expect(snackBarServiceStub.openSnack).toHaveBeenCalled();
  });

  it('should close the dialog', () => {
    component.close();
    expect(matDialogRefStub.close).toHaveBeenCalledWith();
  });

  it('should close the dialog with refresh return', () => {
    component.closeWithRefresh();
    expect(matDialogRefStub.close).toHaveBeenCalledWith(true);
  });

  it('should have a password field', () => {
    const pw = fixture.debugElement.nativeElement.querySelector('input[name=password][validateEqual=confirmPassword][type=password][required]');
    expect(pw).toBeTruthy();
  });

  it('should have a confirm password field', () => {
    const cpw = fixture.debugElement.nativeElement.querySelector('input[name=confirmPassword][validateEqual=password][type=password][required]');
    expect(cpw).toBeTruthy();
  });

  it('should have an apply button', () => {
    const apply = fixture.debugElement.nativeElement.querySelector('button[name=submit][type=submit]');
    expect(apply).toBeTruthy();
  });

  it('should have a cancel button', () => {
    const cancel = fixture.debugElement.nativeElement.querySelector('button[name=cancel][type=button]');
    expect(cancel).toBeTruthy();
  });

});
