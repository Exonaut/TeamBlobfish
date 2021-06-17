import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { config } from '../../../core/config/config';
import { ResetPasswordComponent } from './reset-password.component';
import { ConfigService } from '../../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { SnackBarService } from '../../../../app/core/snack-bar/snack-bar.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from '../../../core/core.module';
import { provideMockStore } from '@ngrx/store/testing';
import { getTranslocoModule } from '../../../transloco-testing.module';

const routerStub = {

}

const activatedRouteStub = {

}

class TestBedSetUp {
  static loadWaiterCockpitServiceStud(waiterCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [ResetPasswordComponent],
      providers: [
        { provide: Router, useValue: routerStub },
        { provide: ActivatedRoute, useValue: activatedRouteStub },
        SnackBarService,
        TranslocoService,
        ConfigService,
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
        RouterTestingModule,
      ],
    });
  }
}
