import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookTableConfirmDialogComponent } from './book-table-confirm-dialog.component';

describe('BookTableConfirmDialogComponent', () => {
  let component: BookTableConfirmDialogComponent;
  let fixture: ComponentFixture<BookTableConfirmDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookTableConfirmDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookTableConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
