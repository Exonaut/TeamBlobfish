import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
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

@Component({
  selector: 'app-user-cockpit',
  templateUrl: './user-cockpit.component.html',
  styleUrls: ['./user-cockpit.component.scss']
})
export class UserCockpitComponent implements OnInit, OnDestroy {
  private translocoSubscription = Subscription.EMPTY;
  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };
  private sorting: ISort[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  users: UserListView[] = [];
  totalUsers: number;

  columns: any[];

  displayedColumns: string[] = [
    'user.id',
    'user.name',
    'user.email',
  ];

  pageSizes: number[];

  filters: FilterUserCockpit = {
    name: undefined,
    email: undefined,
  };

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private configService: ConfigService,
    private route: ActivatedRoute,
    private userCockpitService: UserCockpitService,
  ) {
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.applyFilters();
  }

  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.user', {}, lang)
      .subscribe((userTable) => {
        this.columns = [
          { name: 'user.id', label: userTable.idH },
          { name: 'user.name', label: userTable.nameH },
          { name: 'user.email', label: userTable.emailH },
        ];
      });
  }

  /** Get Orders from backend meeting current filter requirements */
  applyFilters(): void {
    this.userCockpitService
      .getUsers(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.users = [];
        } else {
          this.users = data.content;
        }
        this.totalUsers = data.totalElements;
      });
  }

  /** Clear filters */
  clearFilters(filters: NgForm): void {
    filters.reset();
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

  selected(selection: UserListView): void {
    // this.dialog.open(OrderDialogComponent, {
    //   width: '80%',
    //   data: selection,
    // }).afterClosed().subscribe((data: boolean) => {
    //   if (data === true) { // Reload orders if dialog was edited
    //     this.applyFilters();
    //   }
    // });
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }
}

