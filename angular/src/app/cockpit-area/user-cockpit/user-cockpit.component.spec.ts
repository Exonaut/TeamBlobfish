import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCockpitComponent } from './user-cockpit.component';

describe('UserCockpitComponent', () => {
  let component: UserCockpitComponent;
  let fixture: ComponentFixture<UserCockpitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserCockpitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCockpitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
