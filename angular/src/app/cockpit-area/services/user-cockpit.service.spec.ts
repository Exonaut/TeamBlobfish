import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, inject, TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';
import { ConfigService } from 'app/core/config/config.service';
import { CoreModule } from 'app/core/core.module';
import {
  FilterUserCockpit,
  Pageable,
  Sort,
} from 'app/shared/backend-models/interfaces';
import { UserListView } from 'app/shared/view-models/interfaces';
import { of } from 'rxjs';
import { config } from '../../core/config/config';
import { UserCockpitService } from './user-cockpit.service';

const configServiceStub = {
  getRestServiceRoot: jasmine
    .createSpy('getRestServiceRoot')
    .and.returnValue(of('http://localhost:8081/mythaistar/services/rest/')),
};

const httpClientStub = {
  patch: jasmine.createSpy('patch').and.returnValue(of({})),
  post: jasmine.createSpy('post').and.returnValue(of({})),
  delete: jasmine.createSpy('delete').and.returnValue(of({})),
  get: jasmine.createSpy('get').and.returnValue(of({})),
};

describe('UserCockpitService', () => {
  let initialState;
  let configService: ConfigService;
  let httpClient: HttpClient;
  let service: UserCockpitService;

  beforeEach(async(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({ initialState }),
        { provide: ConfigService, useValue: configServiceStub },
        { provide: HttpClient, useValue: httpClientStub },
        UserCockpitService,
      ],
      imports: [CoreModule],
    });
    service = TestBed.inject(UserCockpitService);
    configService = TestBed.inject(ConfigService);
    httpClient = TestBed.inject(HttpClient);
  }));

  it('should create', async(() => {
    expect(service).toBeTruthy();
  }));

  it('should get users', async(() => {
    const pageable: Pageable = {
      pageSize: 8,
      pageNumber: 0,
    };
    const sorting: Sort[] = [{ property: undefined, direction: undefined }];
    const filters: FilterUserCockpit = {
      email: undefined,
      id: undefined,
      userRoleId: undefined,
      username: undefined,
    };
    service.getUsers(pageable, sorting, filters).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.post).toHaveBeenCalled();
  }));

  it('should delete user', async(() => {
    service.deleteUser(0).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.delete).toHaveBeenCalledWith(
      'http://localhost:8081/mythaistar/services/rest/usermanagement/v1/user/0',
    );
  }));

  it('should create user', async(() => {
    const user: UserListView = {
      id: 0,
      username: 'user',
      email: 'email',
      userRoleId: 0,
      password: 'password',
    };
    service.createUser(user).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.post).toHaveBeenCalledWith(
      'http://localhost:8081/mythaistar/services/rest/usermanagement/v1/user',
      user,
    );
  }));

  it('should send password reset link', async(() => {
    const user: UserListView = {
      id: 0,
      username: 'user',
      email: 'email',
      userRoleId: 0,
      password: 'password',
    };
    service.sendPasswordResetLink(user).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.get).toHaveBeenCalledWith(
      'http://localhost:8081/mythaistar/services/rest/usermanagement/v1/user/' +
        user.email,
    );
  }));

  it('should edit user', async(() => {
    const user: UserListView = {
      id: 0,
      username: 'user',
      email: 'email',
      userRoleId: 0,
      password: 'password',
    };
    service.editUser(user).subscribe();
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.patch).toHaveBeenCalledWith(
      'http://localhost:8081/mythaistar/services/rest/usermanagement/v1/user/edit',
      user,
    );
  }));
});
