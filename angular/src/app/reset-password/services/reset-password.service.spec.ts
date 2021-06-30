import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { async, getTestBed, inject, TestBed } from '@angular/core/testing';
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

const resetPasswordServiceStub = {
    success: true,
};

describe('SidenavSharedService', () => {
  let resetPasswordService: ResetPasswordService;
  let httpTestingController: HttpTestingController;
  let mockStore: MockStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        { provide: ConfigService, useValue: configServiceStub },
        ResetPasswordService,
        provideMockStore(),
      ],
      imports: [CoreModule, HttpClientTestingModule],
    });
    resetPasswordService = TestBed.inject(ResetPasswordService);
    httpTestingController = TestBed.inject(HttpTestingController);
    mockStore = TestBed.inject(MockStore);
  });

  afterEach(() => {
      httpTestingController.verify();
  });

  it('should create', inject(
    [ResetPasswordService],
    (service: ResetPasswordService) => {
      expect(service).toBeTruthy();
    },
  ));

});
