import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TranslocoService } from '@ngneat/transloco';
import { UserCockpitService } from 'app/cockpit-area/services/user-cockpit.service';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { UserListView } from 'app/shared/view-models/interfaces';
import { clone } from 'lodash';
import * as moment from 'moment';

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.scss']
})
export class ChangePasswordDialogComponent implements OnInit {

  data: UserListView;

  roleNames: any[];
  selectedRole: number;

  title: any = {
    username: "",
    id: -1,
  }

  constructor(
    public dialog: MatDialogRef<ChangePasswordDialogComponent>,
    private userCockpitService: UserCockpitService,
    private translocoService: TranslocoService,
    private snack: SnackBarService,
    @Inject(MAT_DIALOG_DATA) dialogData: UserListView,
  ) {
    this.data = clone(dialogData);
    this.title.username = this.data.username;
    this.title.id = this.data.id;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setRoleNames(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.selectedRole = this.data.userRoleId;
    this.data.password = null;
  }

  editUser(value: any): void {
    this.data.password = value.password;
    this.userCockpitService
    .editUser(this.data)
    .subscribe(
      ($data: any) => {
        this.snack.openSnack(
          this.translocoService.translate('cockpit.user.changePasswordSuccess'),
          6000,
          'green'
        );
        this.closeWithRefresh();
      },
      (error) => {
        this.snack.openSnack(
          this.translocoService.translate('cockpit.user.changePasswordError'),
          6000,
          'red'
        );
      }
    );
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

  close(): void {
    this.dialog.close();
  }

  closeWithRefresh(): void {
    this.dialog.close(true);
  }

}
