import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { of } from 'rxjs/internal/observable/of';
import { UserDialogComponent } from './user-dialog.component';
import { config } from '../../../core/config/config';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../../store';
import { ConfigService } from '../../../core/config/config.service';
import { UserCockpitService } from '../../services/user-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { provideMockStore } from '@ngrx/store/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getTranslocoModule } from '../../../transloco-testing.module';
import { CoreModule } from '../../../core/core.module';
import { GetAllUsersData } from '../../../../in-memory-test-data/db-users';
import { DebugElement } from '@angular/core';
import { ChangePasswordDialogComponent } from './change-password-dialog/change-password-dialog.component';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';

const translocoServiceStub = {
  selectTranslateObject:
    of({
      idH: 'ID',
      nameH: 'Name',
      emailH: 'E-Mail',
      roleH: 'Role',
      guest: 'Guest',
      waiter: 'Waiter',
      manager: 'Manager',
      admin: 'Admin',
    } as any),
};

const mockDialog = {
  open: jasmine.createSpy('open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

const userCockpitServiceStub = {
  getUsers: jasmine.createSpy('getUsers').and.returnValue(
    of({content: GetAllUsersData}),
  ),
  sendPasswordResetLink: jasmine.createSpy('sendPasswordResetLink').and.returnValue(
    of({}),
  ),
};

const matDialogRefStub = {
  close: jasmine.createSpy('close'),
};

const snackBarServiceStub = {
  openSnack: jasmine.createSpy('openSnack'),
};

class TestBedSetUp {
  static loadUserCockpitServiceStud(userCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [UserDialogComponent],
      providers: [
        { provide: MatDialog, useValue: mockDialog },
        { provide: UserCockpitService, useValue: userCockpitStub },
        { provide: MAT_DIALOG_DATA, useValue: GetAllUsersData[0]},
        { provide: MatDialogRef, useValue: matDialogRefStub},
        { provide: SnackBarService, useValue: snackBarServiceStub},
        ConfigService,
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
  let component: UserDialogComponent;
  let fixture: ComponentFixture<UserDialogComponent>;
  let store: Store<State>;
  let initialState;
  let userCockpitService: UserCockpitService;
  let dialog: MatDialog;
  let configService: ConfigService;
  let el: DebugElement;
  let translocoService: TranslocoService;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadUserCockpitServiceStud(userCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(UserDialogComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        userCockpitService = TestBed.inject(UserCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
        spyOn(translocoService, 'selectTranslateObject').and.returnValue(
          translocoServiceStub.selectTranslateObject
        );
      });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create component and verify content of UserDialog', fakeAsync(() => {
  //   fixture.detectChanges();
  //   tick();
  //   expect(component).toBeTruthy();
  //   expect(component.data).toEqual(GetAllUsersData[0]);
  // }));

  // // Translation labels
  // it('should verify table header names', () => {
  //   expect(component.columns[0].label === 'ID').toBeTruthy();
  //   expect(component.columns[1].label === 'Name').toBeTruthy();
  //   expect(component.columns[2].label === 'E-Mail').toBeTruthy();
  //   expect(component.columns[3].label === 'Role').toBeTruthy();
  // });

  // it('should verify role names', () => {
  //   expect(component.roleNames[0].label === 'Guest').toBeTruthy();
  //   expect(component.roleNames[1].label === 'Waiter').toBeTruthy();
  //   expect(component.roleNames[2].label === 'Manager').toBeTruthy();
  //   expect(component.roleNames[3].label === 'Admin').toBeTruthy();
  // });

  // // Dialog related functions
  // it('should close the dialog', () => {
  //   component.close();
  //   expect(matDialogRefStub.close).toHaveBeenCalledWith();
  // });

  // it('should close the dialog with refresh return', () => {
  //   component.closeWithRefresh();
  //   expect(matDialogRefStub.close).toHaveBeenCalledWith(true);
  // });

  // it('should open the change password dialog with data', () => {
  //   component.openChangePasswordDialog();
  //   expect(mockDialog.open).toHaveBeenCalled();
  //   expect(mockDialog.open.calls.mostRecent().args[0] === ChangePasswordDialogComponent).toBeTruthy();
  //   expect(mockDialog.open.calls.mostRecent().args[1] instanceof Object).toBeTruthy();
  //   expect(mockDialog.open.calls.mostRecent().args[1].data !== null).toBeTruthy();
  // });

  // // User-service related functions
  // it('should send password reset link and open snack', () => {
  //   component.sendPasswordResetLink();
  //   expect(userCockpitServiceStub.sendPasswordResetLink).toHaveBeenCalledWith(component.data);
  //   expect(snackBarServiceStub.openSnack).toHaveBeenCalled();
  // });

  // // Columns
  // it('should have an ID column', () => {
  //   const header = fixture.debugElement.nativeElement.querySelector('th.idHeader');
  //   expect(header).toBeTruthy();
  //   const data = fixture.debugElement.nativeElement.querySelector('td.idData');
  //   expect(data).toBeTruthy();
  // });

  // it('should have a Name column', () => {
  //   const header = fixture.debugElement.nativeElement.querySelector('th.nameHeader');
  //   expect(header).toBeTruthy();
  //   const data = fixture.debugElement.nativeElement.querySelector('td.nameData');
  //   expect(data).toBeTruthy();
  // });

  // it('should have a E-Mail column', () => {
  //   const header = fixture.debugElement.nativeElement.querySelector('th.emailHeader');
  //   expect(header).toBeTruthy();
  //   const data = fixture.debugElement.nativeElement.querySelector('td.emailData');
  //   expect(data).toBeTruthy();
  // });

  // it('should have a Role column', () => {
  //   const header = fixture.debugElement.nativeElement.querySelector('th.roleHeader');
  //   expect(header).toBeTruthy();
  //   const data = fixture.debugElement.nativeElement.querySelector('td.roleData');
  //   expect(data).toBeTruthy();
  // });

  // // Buttons
  // it('should have a Delete User Button', () => {
  //   const header = fixture.debugElement.nativeElement.querySelector(
  //     'button[name=deleteUser][type=button][mat-dialog-close]'
  //   );
  //   expect(header).toBeTruthy();
  // });

  // it('should have a Send Reset Password Link Button', () => {
  //   const header = fixture.debugElement.nativeElement.querySelector('button[name=sendPasswordResetLink][type=button]');
  //   expect(header).toBeTruthy();
  // });

  // it('should have a Close Dialog Button', () => {
  //   const header = fixture.debugElement.nativeElement.querySelector('button[name=close][type=button][mat-dialog-close]');
  //   expect(header).toBeTruthy();
  // });

});
