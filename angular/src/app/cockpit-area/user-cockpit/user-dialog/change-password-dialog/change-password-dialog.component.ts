import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TranslocoService } from '@ngneat/transloco';
import { UserCockpitService } from 'app/cockpit-area/services/user-cockpit.service';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { UserListView } from 'app/shared/view-models/interfaces';

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.scss']
})
export class ChangePasswordDialogComponent implements OnInit {

  data: UserListView;

  constructor(
    public dialog: MatDialogRef<ChangePasswordDialogComponent>,
    private userCockpitService: UserCockpitService,
    private translocoService: TranslocoService,
    private snack: SnackBarService,
    @Inject(MAT_DIALOG_DATA) dialogData: UserListView,
  ) {
    this.data = dialogData;
  }

  ngOnInit(): void {

  }

  changePassword(value: any): void {
    this.data.password = value.password;
    this.userCockpitService
    .changePassword(this.data)
    .subscribe((data: any) => {
      this.snack.openSnack(
        this.translocoService.translate('cockpit.user.changePasswordSuccess'),
        6000,
        'green'
      );
      this.closeWithRefresh();
    });
  }

  close(): void {
    this.dialog.close();
  }

  closeWithRefresh(): void {
    this.dialog.close(true);
  }

}
