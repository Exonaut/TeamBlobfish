import { HttpClientTestingModule } from '@angular/common/http/testing';
import { inject, TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';
import { ConfigService } from 'app/core/config/config.service';
import { config } from '../../core/config/config';
import { UserCockpitService } from './user-cockpit.service';

describe('UserCockpitService', () => {
  let initialState;
  beforeEach(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({ initialState }),
        UserCockpitService,
        ConfigService,
      ],
      imports: [HttpClientTestingModule],
    });
  });

  it('should create', inject(
    [UserCockpitService],
    (service: UserCockpitService) => {
      expect(service).toBeTruthy();
    },
  ));
});
