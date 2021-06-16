import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from 'app/core/config/config.service';
import {
  FilterUserCockpit,
  Pageable,
  Sort,
} from 'app/shared/backend-models/interfaces';
import { UserListView } from 'app/shared/view-models/interfaces';
import { Observable, throwError } from 'rxjs';
import { catchError, exhaustMap } from 'rxjs/operators';

@Injectable()
export class UserCockpitService {

  private readonly getUsersRestPath: string =
    'usermanagement/v1/user/search';
  private readonly filterUsersRestPath: string =
    'usermanagement/v1/user/search';
  private readonly deleteUserRestPath: string =
    'usermanagement/v1/user';
  private readonly createUserRestPath: string =
    'usermanagement/v1/user';
  private readonly changePasswordRestPath: string =
    'usermanagement/v1/user/reset/password/admin';
  private readonly sendPasswordResetLinkRestPath: string =
    'usermanagement/v1/user';
  private readonly editUserRestPath: string =
    'usermanagement/v1/user/edit';

    private readonly restServiceRoot$: Observable<
      string
    > = this.config.getRestServiceRoot();

  constructor(
    private http: HttpClient,
    private config: ConfigService,
  ) { }

  getUsers(
    pageable: Pageable,
    sorting: Sort[],
    filters: FilterUserCockpit,
  ): Observable<UserListView[]> {
    let path: string;
    filters.pageable = pageable;
    filters.pageable.sort = sorting;
    if (filters.email || filters.username || filters.userRoleId ) {
      path = this.filterUsersRestPath;
    } else {
      delete filters.email;
      delete filters.username;
      delete filters.userRoleId;
      path = this.getUsersRestPath;
    }
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<UserListView[]>(`${restServiceRoot}${path}`, filters),
      ),
    );
  }

  deleteUser(id: number): Observable<any> {
    let path: string;
    path = this.deleteUserRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.delete<UserListView[]>(`${restServiceRoot}${path}/${id}`).pipe(
          catchError(err => this.errorHandler(err))
        ),
      ),
    );
  }

  errorHandler(error: HttpErrorResponse): Observable<any> {
    return Observable.throw(error.message || 'Server Error');
  }

  createUser(value: any): Observable<any> {
    let path: string;
    path = this.createUserRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<any>(`${restServiceRoot}${path}`, value).pipe(
          catchError(err => this.errorHandler(err))
        ),
      ),
    );
  }

  changePassword(user: UserListView): Observable<any> {
    let path: string;
    path = this.changePasswordRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.patch<any>(`${restServiceRoot}${path}`, user).pipe(
          catchError(err => this.errorHandler(err))
        ),
      ),
    );
  }

  sendPasswordResetLink(user: UserListView): Observable<any> {
    let path: string;
    path = this.sendPasswordResetLinkRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.get<any>(`${restServiceRoot}${path}/${user.email}`).pipe(
          catchError(err => this.errorHandler(err))
        ),
      ),
    );
  }

  editUser(user: UserListView): Observable<any> {
    let path: string;
    path = this.editUserRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.patch<any>(`${restServiceRoot}${path}`, user).pipe(
          catchError(err => this.errorHandler(err))
        ),
      ),
    );
  }

}
