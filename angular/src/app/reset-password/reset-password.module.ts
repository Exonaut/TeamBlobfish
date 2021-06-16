import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreModule } from '../core/core.module';
import { HttpClientModule } from '@angular/common/http';
import { ResetPasswordComponent } from './container/reset-password/reset-password.component';

@NgModule({
  imports: [
    CommonModule,
    CoreModule,
    HttpClientModule,
  ],
  providers: [ResetPasswordComponent],
  declarations: [ResetPasswordComponent],
  exports: [ResetPasswordComponent],
})
export class ResetPasswordModule {}
