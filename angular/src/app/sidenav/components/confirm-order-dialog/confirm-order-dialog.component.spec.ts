import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from 'app/core/core.module';
import { getTranslocoModule } from 'app/transloco-testing.module';

import { ConfirmOrderDialogComponent } from './confirm-order-dialog.component';

describe('ConfirmOrderDialogComponent', () => {
  let component: ConfirmOrderDialogComponent;
  let fixture: ComponentFixture<ConfirmOrderDialogComponent>;

  const matDialogRefStub = {

  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmOrderDialogComponent ],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {

         } },
        { provide: MatDialogRef, useValue: matDialogRefStub}
      ],
      imports: [
        BrowserAnimationsModule,
        CoreModule,
        getTranslocoModule()
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmOrderDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
