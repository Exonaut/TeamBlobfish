import { TestBed } from '@angular/core/testing';

import { UserCockpitService } from './user-cockpit.service';

describe('UserCockpitService', () => {
  let service: UserCockpitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserCockpitService);
  });

  // it('should be created', () => {
  //   expect(service).toBeTruthy();
  // });
});
