import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { TranslocoService } from '@ngneat/transloco';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { ResetPasswordService } from '../../services/reset-password.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {

  private token: number;

  constructor(
    private resetPasswordService: ResetPasswordService,
    private translocoService: TranslocoService,
    private router: Router,
    private route: ActivatedRoute,
    private snack: SnackBarService,
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.token = +params.get('token');
    });
  }

  resetPassword(pw: string): void {
    this.resetPasswordService.resetPassword(pw, this.token).subscribe(
      ($data) => {
        this.snack.openSnack(
          this.translocoService.translate('cockpit.user.createUserSuccess'),
          6000,
          'green'
        );
        this.router.navigate(['/restaurant']);
      },
      (error) => {
        this.snack.openSnack(
          this.translocoService.translate('cockpit.user.createUserError'),
          6000,
          'red'
        );
      });
  }

}
