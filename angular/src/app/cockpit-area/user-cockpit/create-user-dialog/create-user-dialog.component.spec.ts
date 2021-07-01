import { DebugElement } from '@angular/core';
import {
  async,
  ComponentFixture,
  fakeAsync,
  TestBed,
  tick,
} from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { By } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslocoService } from '@ngneat/transloco';
import { provideMockStore } from '@ngrx/store/testing';
import { UserCockpitService } from 'app/cockpit-area/services/user-cockpit.service';
import { ConfigService } from 'app/core/config/config.service';
import { CoreModule } from 'app/core/core.module';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { click } from 'app/shared/common/test-utils';
import { getTranslocoModule } from 'app/transloco-testing.module';
import { GetAllUsersData } from 'in-memory-test-data/db-users';
import { of } from 'rxjs';
import { config } from '../../../core/config/config';

import { CreateUserDialogComponent } from './create-user-dialog.component';

const userCockpitServiceStub = {
  getUsers: jasmine
    .createSpy('getUsers')
    .and.returnValue(of({ content: GetAllUsersData, totalElements: 1 })),
  deleteUser: jasmine.createSpy('deleteUser').and.returnValue(of({})),
  createUser: jasmine.createSpy('createUser').and.returnValue(of({})),
  changePassword: jasmine.createSpy('changePassword').and.returnValue(of({})),
  resetPassword: jasmine.createSpy('resetPassword').and.returnValue(of({})),
  sendPasswordResetLink: jasmine
    .createSpy('sendPasswordResetLink')
    .and.returnValue(of({})),
  editUser: jasmine.createSpy('editUser').and.returnValue(of({})),
};

const mockDialogRef = {
  close: jasmine.createSpy('close'),
};

const snackBarStub = {
  openSnack: jasmine.createSpy('openSnack'),
};

describe('CreateUserDialogComponent', () => {
  let component: CreateUserDialogComponent;
  let fixture: ComponentFixture<CreateUserDialogComponent>;
  let initialState;
  let userCockpitService: UserCockpitService;
  let dialog: MatDialogRef<CreateUserDialogComponent>;
  let translocoService: TranslocoService;
  let el: DebugElement;
  let snack: SnackBarService;

  beforeEach(async(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      declarations: [CreateUserDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: mockDialogRef },
        { provide: UserCockpitService, useValue: userCockpitServiceStub },
        { provide: SnackBarService, useValue: snackBarStub },
        TranslocoService,
        ConfigService,
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
      ],
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(CreateUserDialogComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        userCockpitService = TestBed.inject(UserCockpitService);
        dialog = TestBed.inject(MatDialogRef);
        translocoService = TestBed.inject(TranslocoService);
        snack = TestBed.inject(SnackBarService);
      });
  }));

  it('should create', async(() => {
    expect(component).toBeTruthy();
  }));

  it('should create user', async(() => {
    component.createUser({});
    expect(userCockpitService.createUser).toHaveBeenCalled();
    expect(snack.openSnack).toHaveBeenCalled();
  }));

  it('should close dialog', async(() => {
    component.close();
    expect(dialog.close).toHaveBeenCalled();
  }));

  it('should close dialog with refresh return', async(() => {
    component.closeWithRefresh();
    expect(dialog.close).toHaveBeenCalledWith(true);
  }));

  it('should set role names', async(() => {
    spyOn(translocoService, 'selectTranslateObject').and.returnValue(of([]));
    component.setRoleNames('en');
    expect(translocoService.selectTranslateObject).toHaveBeenCalled();
  }));
});
