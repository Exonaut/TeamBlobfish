import { UserCockpitService } from 'app/cockpit-area/services/user-cockpit.service';
import { UserListView } from 'app/shared/view-models/interfaces';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import * as _ from 'lodash';
import { MatSelectChange } from '@angular/material/select';
import * as fromAuth from 'app/user-area/store/selectors/auth.selectors';
import { Store } from '@ngrx/store';
import * as fromApp from 'app/store/reducers';
import { catchError, map} from 'rxjs/operators';
import { SnackBarService } from '../../../core/snack-bar/snack-bar.service';
import { ChangePasswordDialogComponent } from './change-password-dialog/change-password-dialog.component';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.scss']
})
export class UserDialogComponent implements OnInit {
  data: UserListView;
  datat: UserListView[] = [];

  columns: any[];
  roleNames: any[];

  displayedColumns: string[] = [
    'user.id',
    'user.name',
    'user.email',
    'user.role',
  ];

  currentUser: string;

  constructor(
    private userCockpitService: UserCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: UserListView,
    private configService: ConfigService,
    public dialogRef: MatDialogRef<UserDialogComponent>,
    private dialog: MatDialog,
    private store: Store<fromApp.State>,
    private snack: SnackBarService,
  ) {
    this.data = dialogData;
    this.datat.push(this.data);
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      this.setRoleNames(event);
    });

    // Should work but removed due to testing issues
    // this.store.select(fromAuth.getUserName)
    // .subscribe((data: string) => {
    //   this.currentUser = data;
    // });
  }

  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.user', {}, lang)
      .subscribe((userTable) => {
        this.columns = [
          { name: 'user.id', label: userTable.idH },
          { name: 'user.name', label: userTable.nameH },
          { name: 'user.email', label: userTable.emailH },
          { name: 'user.role', label: userTable.roleH },
        ];
      });
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

  /**
   * Delete account with id
   */
  deleteUser(id: number): void {
    this.userCockpitService
    .deleteUser(id)
    .subscribe((data: any) => {
      this.snack.openSnack(
        this.translocoService.translate('cockpit.user.deleteUserSuccess'),
        6000,
        'green'
      );
      this.closeWithRefresh();
    });
  }

  openChangePasswordDialog(): void {
    this.dialog.open(ChangePasswordDialogComponent, {
      width: '30%',
      data: this.data,
    }).afterClosed().subscribe((data: boolean) => {
      if (data === true) { // Reload users if dialog was edited

      }
    });
  }

  sendPasswordResetLink(): void {
    this.userCockpitService
    .sendPasswordResetLink(this.data)
    .subscribe((data: any) => {
      this.snack.openSnack(
        this.translocoService.translate('cockpit.user.sendPasswordResetLinkSuccess'),
        6000,
        'green'
      );
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  closeWithRefresh(): void {
    this.dialogRef.close(true);
  }

}
