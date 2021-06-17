import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from 'app/core/config/config.service';
import { Observable, throwError } from 'rxjs';
import { catchError, exhaustMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ResetPasswordService {

  private readonly resetPasswordRestPath: string =
    'usermanagement/v1/user/reset/password';

  private readonly restServiceRoot$: Observable<
    string
  > = this.config.getRestServiceRoot();

  constructor(
    private http: HttpClient,
    private config: ConfigService,
  ) { }

  resetPassword(pw: string, token: number): Observable<any> {
    let path: string;
    path = this.resetPasswordRestPath;
    return this.restServiceRoot$.pipe(
      exhaustMap((restServiceRoot) =>
        this.http.patch<any>(`${restServiceRoot}${path}/${token}/${pw}`, {}).pipe(
          catchError(err => this.errorHandler(err))
        ),
      ),
    );
  }

  errorHandler(error: HttpErrorResponse): Observable<any> {
    return throwError(error.message || 'Server Error');
  }
}
