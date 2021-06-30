import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TranslocoService } from '@ngneat/transloco';
import { UserCockpitService } from 'app/cockpit-area/services/user-cockpit.service';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import * as moment from 'moment';

@Component({
  selector: 'app-create-user-dialog',
  templateUrl: './create-user-dialog.component.html',
  styleUrls: ['./create-user-dialog.component.scss']
})
export class CreateUserDialogComponent implements OnInit {

  roleNames: any[];
  selectedRole: number;

  constructor(
    public dialog: MatDialogRef<CreateUserDialogComponent>,
    private userCockpitService: UserCockpitService,
    private translocoService: TranslocoService,
    private snack: SnackBarService,
  ) { }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setRoleNames(event);
      moment.locale(this.translocoService.getActiveLang());
    });
  }

  /**
   * Create user
   * @param value The Form value to create the user from
   */
  createUser(value: any): void {
    delete value.confirmPassword;
    value.userRoleId = this.selectedRole;
    this.userCockpitService.createUser(value)
      .subscribe(
        ($data) => {
          this.snack.openSnack(
            this.translocoService.translate('cockpit.user.createUserSuccess'),
            6000,
            'green'
          );
          this.closeWithRefresh();
        },
        (error) => {
          this.snack.openSnack(
            this.translocoService.translate('cockpit.user.createUserError'),
            6000,
            'red'
          );
        }
      );
  }

  close(): void {
    this.dialog.close();
  }

  closeWithRefresh(): void {
    this.dialog.close(true);
  }

  setRoleNames(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.user', {}, lang)
      .subscribe((userTable) => {
        this.roleNames = [
          { name: 'user.guest', label: userTable.guest },
          { name: 'user.waiter', label: userTable.waiter },
          { name: 'user.manager', label: userTable.manager },
          { name: 'user.admin', label: userTable.admin },
        ];
      });
  }

}
