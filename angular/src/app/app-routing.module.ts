import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrderCockpitComponent } from './cockpit-area/order-cockpit/order-cockpit.component';
import { ReservationCockpitComponent } from './cockpit-area/reservation-cockpit/reservation-cockpit.component';
import { UserCockpitComponent } from './cockpit-area/user-cockpit/user-cockpit.component';
import { AuthGuardService } from './core/authentication/auth-guard.service';
import { NotFoundComponent } from './core/not-found/not-found.component';
import { NotSupportedComponent } from './core/not-supported/not-supported.component';
import { EmailConfirmationsComponent } from './email-confirmations/container/email-confirmations/email-confirmations.component';
import { HomeComponent } from './home/container/home/home.component';
import { MenuComponent } from './menu/container/menu.component';
import { ResetPasswordComponent } from './reset-password/container/reset-password/reset-password.component';

const appRoutes: Routes = [
  { path: 'restaurant', component: HomeComponent, pathMatch: 'full' },
  { path: 'menu', component: MenuComponent },
  { path: 'bookTable', redirectTo: '/menu' },
  { path: 'booking/:action/:token', component: EmailConfirmationsComponent },
  { path: 'user/resetpassword/:token', component: ResetPasswordComponent },
  {
    path: 'orders',
    data: { ['archive']: false }, // Disable archive mode
    component: OrderCockpitComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'archive',
    data: { ['archive']: true }, // Enable archive mode
    component: OrderCockpitComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'reservations',
    component: ReservationCockpitComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'prediction',
    component: NotSupportedComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'clustering',
    component: NotSupportedComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: 'users',
    component: UserCockpitComponent,
    canActivate: [AuthGuardService],
  },
  { path: '', redirectTo: '/restaurant', pathMatch: 'full' },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true }, // <-- debugging purposes only
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
