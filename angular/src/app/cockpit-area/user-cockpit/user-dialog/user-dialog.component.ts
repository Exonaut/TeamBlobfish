import { UserCockpitService } from 'app/cockpit-area/services/user-cockpit.service';
import { UserListView } from 'app/shared/view-models/interfaces';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import * as _ from 'lodash';
import { MatSelectChange } from '@angular/material/select';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.scss']
})
export class UserDialogComponent implements OnInit {
  data: UserListView;
  datat: UserListView[] = [];

  columns: any[];
  displayedColumns: string[] = [
    'id',
    'name',
    'email',
  ];

  constructor(
    private waiterCockpitService: UserCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: UserListView,
    private configService: ConfigService,
    public dialog: MatDialogRef<UserDialogComponent>,
  ) {
    this.data = dialogData;
    this.datat.push(this.data);
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });
  }

  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.user', {}, lang)
      .subscribe((userTable) => {
        this.columns = [
          { name: 'user.id', label: userTable.idH },
          { name: 'user.name', label: userTable.nameH },
          { name: 'user.email', label: userTable.emailH },
        ];
      });
  }

  applyChanges(): void {
    this.dialog.close();
  }

}
