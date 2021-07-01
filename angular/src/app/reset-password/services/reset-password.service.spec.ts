import { HttpClient } from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import {
  async,
  fakeAsync,
  getTestBed,
  inject,
  TestBed,
} from '@angular/core/testing';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { ConfigService } from 'app/core/config/config.service';
import { CoreModule } from 'app/core/core.module';
import { of } from 'rxjs';
import { config } from '../../core/config/config';
import { ResetPasswordService } from './reset-password.service';

const configServiceStub = {
  getRestServiceRoot: jasmine
    .createSpy('getRestServiceRoot')
    .and.returnValue(of('http://localhost:8081/mythaistar/services/rest/')),
};

const httpClientStub = {
  patch: jasmine.createSpy('patch').and.returnValue(of({})),
};

describe('SidenavSharedService', () => {
  let resetPasswordService: ResetPasswordService;
  let httpClient: HttpClient;
  let mockStore: MockStore;
  let configService: ConfigService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: ConfigService, useValue: configServiceStub },
        { provide: HttpClient, useValue: httpClientStub },
        provideMockStore(),
      ],
      imports: [CoreModule],
    });
    resetPasswordService = TestBed.inject(ResetPasswordService);
    httpClient = TestBed.inject(HttpClient);
    configService = TestBed.inject(ConfigService);
    mockStore = TestBed.inject(MockStore);
  }));

  it('should create', inject(
    [ResetPasswordService],
    (service: ResetPasswordService) => {
      expect(service).toBeTruthy();
    },
  ));

  it('should reset password', async(() => {
    spyOn(resetPasswordService, 'errorHandler');
    resetPasswordService
      .resetPassword('Password', 1234)
      .subscribe((resp: any) => {});
    expect(configService.getRestServiceRoot).toHaveBeenCalled();
    expect(httpClient.patch).toHaveBeenCalledWith(
      'http://localhost:8081/mythaistar/services/rest/usermanagement/v1/user/reset/password/1234/Password',
      {},
    );
  }));
});
