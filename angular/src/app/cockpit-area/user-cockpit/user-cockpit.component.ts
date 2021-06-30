import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, NgForm, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { TranslocoService } from '@ngneat/transloco';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { ConfigService } from '../../core/config/config.service';
import {
  FilterUserCockpit,
  Pageable,
  Sort as ISort,
} from '../../shared/backend-models/interfaces';
import { UserListView } from '../../shared/view-models/interfaces';
import * as _ from 'lodash';
import { ActivatedRoute } from '@angular/router';
import { UserCockpitService } from '../services/user-cockpit.service';
import { CreateUserDialogComponent } from './create-user-dialog/create-user-dialog.component';
import { EditUserDialogComponent } from './edit-user-dialog/edit-user-dialog.component';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { Store } from '@ngrx/store';
import { AuthService } from 'app/core/authentication/auth.service';
import { ConfirmDialogComponent } from 'app/shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-user-cockpit',
  templateUrl: './user-cockpit.component.html',
  styleUrls: ['./user-cockpit.component.scss'],
})
export class UserCockpitComponent implements OnInit, OnDestroy {
  private translocoSubscription = Subscription.EMPTY;
  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
  };
  private sorting: ISort[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  users: UserListView[] = [];
  totalUsers: number;

  columns: any[];
  roleNames: any[];

  displayedColumns: string[] = [
    'user.id',
    'user.name',
    'user.email',
    'user.role',
    'user.actions',
  ];

  pageSizes: number[];

  filters: FilterUserCockpit = {
    id: undefined,
    username: undefined,
    email: undefined,
    userRoleId: undefined,
  };

  hoverChangePw: -1;
  hoverResetPw: -1;
  hoverDeleteUser: -1;

  userRoleSelected: FormControl;

  userName: string;

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private configService: ConfigService,
    private route: ActivatedRoute,
    private userCockpitService: UserCockpitService,
    private snack: SnackBarService,
    public auth: AuthService,
  ) {
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      this.setRoleNames(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.applyFilters();
    this.userRoleSelected = new FormControl(this.filters.userRoleId, [
      Validators.required,
    ]);
    this.auth.getUser().subscribe((data) => {
      this.userName = data;
    });
  }

  /**
   * Sets the translations for the table headers
   * @param lang The language to use
   */
  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.user', {}, lang)
      .subscribe((userTable) => {
        this.columns = [
          { name: 'user.id', label: userTable.idH },
          { name: 'user.name', label: userTable.nameH },
          { name: 'user.email', label: userTable.emailH },
          { name: 'user.role', label: userTable.roleH },
          { name: 'user.actions', label: userTable.actionsH },
        ];
      });
  }

  /**
   * Sets the translations for the user roles
   * @param lang The language to use
   */
  setRoleNames(lang: string): void {
    this.translocoSubscription = this.translocoService
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

  /** Get Orders from backend meeting current filter requirements */
  applyFilters(): void {
    this.userCockpitService
      .getUsers(this.pageable, this.sorting, this.filters)
      .subscribe(
        ($data: any) => {
          if (!$data) {
            this.users = [];
          } else {
            this.users = $data.content;
          }
          this.totalUsers = $data.totalElements;
        },
        (error) => {
          this.snack.openSnack(
            this.translocoService.translate('cockpit.user.fetchUsersError'),
            6000,
            'red',
          );
          this.users = [];
        },
      );
  }

  /** Clear filters */
  clearFilters(filters: NgForm): void {
    filters.reset();
    this.filters.userRoleId = undefined;
    this.applyFilters();
    this.pagingBar.firstPage();
  }

  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
    };
    this.applyFilters();
  }

  sort(sortEvent: Sort): void {
    this.sorting = [];
    if (sortEvent.direction) {
      this.sorting.push({
        property: sortEvent.active,
        direction: '' + sortEvent.direction,
      });
    }
    this.applyFilters();
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }

  /**
   * Opens the CreateUserDialogComponent
   */
  openCreateUserDialog(): void {
    this.dialog
      .open(CreateUserDialogComponent)
      .afterClosed()
      .subscribe((data: boolean) => {
        if (data === true) {
          // Reload users if dialog was edited
          this.applyFilters();
        }
      });
  }

  /**
   * Deletes the supplied user with confirmation dialog
   * @param element The Object of the user to delete
   */
  deleteUser(element: any): void {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title:
            element.username +
            ' ( ' +
            this.translocoService.translate('cockpit.user.idH') +
            ': ' +
            element.id +
            ' )',
          text: this.translocoService.translate(
            'cockpit.user.confirmDeleteText',
          ),
        },
      })
      .afterClosed()
      .subscribe((data: boolean) => {
        if (data) {
          this.userCockpitService.deleteUser(element.id).subscribe(
            ($data: any) => (
              this.snack.openSnack(
                this.translocoService.translate(
                  'cockpit.user.deleteUserSuccess',
                ),
                6000,
                'green',
              ),
              this.applyFilters()
            ),
            (error) =>
              this.snack.openSnack(
                this.translocoService.translate('cockpit.user.deleteUserError'),
                6000,
                'red',
              ),
          );
        }
      });
  }

  /**
   * Sends the password reset link to the supplied user with confirm dialog
   * @param element The object of the user to send the link to
   */
  resetPassword(element: any): void {
    this.dialog
      .open(ConfirmDialogComponent, {
        data: {
          title:
            element.username +
            ' ( ' +
            this.translocoService.translate('cockpit.user.idH') +
            ': ' +
            element.id +
            ' )',
          text: this.translocoService.translate(
            'cockpit.user.confirmPasswordLinkText',
          ),
        },
      })
      .afterClosed()
      .subscribe((data: boolean) => {
        if (data) {
          this.userCockpitService.sendPasswordResetLink(element).subscribe(
            ($data: any) => {
              this.snack.openSnack(
                this.translocoService.translate(
                  'cockpit.user.sendPasswordResetLinkSuccess',
                ),
                6000,
                'green',
              );
            },
            (error) => {
              this.snack.openSnack(
                this.translocoService.translate(
                  'cockpit.user.sendPasswordResetLinkError',
                ),
                6000,
                'red',
              );
            },
          );
        }
      });
  }

  /**
   * OPens the EditUserDialogComponent
   * @param element The object of the user to edit
   */
  editUser(element: any): void {
    this.dialog
      .open(EditUserDialogComponent, {
        width: '30%',
        data: element,
      })
      .afterClosed()
      .subscribe((data: boolean) => {
        if (data === true) {
          // Reload users if dialog was edited
          this.applyFilters();
        }
      });
  }
}
