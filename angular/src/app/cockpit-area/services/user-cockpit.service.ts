import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from 'app/core/config/config.service';
import {
  FilterUserCockpit,
  Pageable,
  Sort,
} from 'app/shared/backend-models/interfaces';
import { UserListView } from 'app/shared/view-models/interfaces';
import { Observable } from 'rxjs';
import { exhaustMap } from 'rxjs/operators';

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
    if (filters.email || filters.username || filters.role ) {
      path = this.filterUsersRestPath;
    } else {
      delete filters.email;
      delete filters.username;
      delete filters.role;
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
        this.http.delete<UserListView[]>(`${restServiceRoot}${path}/${id}`),
      ),
    );
  }

  createUser(value: any): Observable<any> {
    let path: string;
    path = this.createUserRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.post<any>(`${restServiceRoot}${path}`, value),
      ),
    );
  }
}
