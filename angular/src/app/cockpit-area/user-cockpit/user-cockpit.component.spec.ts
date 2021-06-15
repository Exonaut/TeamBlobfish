import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { of } from 'rxjs/internal/observable/of';
import { UserCockpitComponent } from './user-cockpit.component';
import { config } from '../../core/config/config';
import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { ConfigService } from '../../core/config/config.service';
import { UserCockpitService } from '../services/user-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { provideMockStore } from '@ngrx/store/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getTranslocoModule } from '../../transloco-testing.module';
import { CoreModule } from '../../core/core.module';
import { GetAllUsersData } from '../../../in-memory-test-data/db-users';
import { DebugElement } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { UserListView } from '../../shared/view-models/interfaces';
import { By } from '@angular/platform-browser';
import { click } from 'app/shared/common/test-utils';
import { AuthService } from 'app/core/authentication/auth.service';

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
    of({content: GetAllUsersData, totalElements: 1}),
  ),
};

const activatedRouteStub = {

};

const authServiceStub = {
  getUser: jasmine.createSpy('getUser').and.returnValue(
    of('admin')
  ),
}

class TestBedSetUp {
  static loadUserCockpitServiceStud(userCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [UserCockpitComponent],
      providers: [
        { provide: MatDialog, useValue: mockDialog },
        { provide: UserCockpitService, useValue: userCockpitStub },
        { provide: ActivatedRoute, useValue: activatedRouteStub},
        { provide: AuthService, useValue: authServiceStub},
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
    });
  }
}

describe('UserCockpitComponent', () => {
  let component: UserCockpitComponent;
  let fixture: ComponentFixture<UserCockpitComponent>;
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
        fixture = TestBed.createComponent(UserCockpitComponent);
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
    fixture = TestBed.createComponent(UserCockpitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component and verify content of UserCockpit', fakeAsync(() => {
    fixture.detectChanges();
    tick();
    expect(component).toBeTruthy();
    expect(component.users).toEqual(GetAllUsersData);
    expect(component.totalUsers).toBe(1);
  }));

  // Translation labels
  it('should verify table header names', () => {
    expect(component.columns[0].label === 'ID').toBeTruthy();
    expect(component.columns[1].label === 'Name').toBeTruthy();
    expect(component.columns[2].label === 'E-Mail').toBeTruthy();
    expect(component.columns[3].label === 'Role').toBeTruthy();
  });

  it('should verify role names', () => {
    expect(component.roleNames[0].label === 'Guest').toBeTruthy();
    expect(component.roleNames[1].label === 'Waiter').toBeTruthy();
    expect(component.roleNames[2].label === 'Manager').toBeTruthy();
    expect(component.roleNames[3].label === 'Admin').toBeTruthy();
  });

});
