import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AssistantsValidatorDirective } from './directives/assistant-validator.directive';
import { EmailValidatorDirective } from './directives/email-validator.directive';
import { EqualValidatorDirective } from './directives/equal-validator.directive';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { CoreModule } from 'app/core/core.module';
import { TranslocoRootModule } from 'app/transloco-root.module';

@NgModule({
  imports: [
    CommonModule,
    CoreModule,
    TranslocoRootModule,
  ],
  declarations: [
    EmailValidatorDirective,
    AssistantsValidatorDirective,
    EqualValidatorDirective,
    ConfirmDialogComponent,
  ],
  exports: [
    EmailValidatorDirective,
    AssistantsValidatorDirective,
    EqualValidatorDirective,
    CommonModule,
  ],
})

export class SharedModule { }
