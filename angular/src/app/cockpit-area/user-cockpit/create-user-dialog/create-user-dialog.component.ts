import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { TranslocoService } from '@ngneat/transloco';
import { UserCockpitService } from 'app/cockpit-area/services/user-cockpit.service';

@Component({
  selector: 'app-create-user-dialog',
  templateUrl: './create-user-dialog.component.html',
  styleUrls: ['./create-user-dialog.component.scss']
})
export class CreateUserDialogComponent implements OnInit {

  constructor(
    public dialog: MatDialogRef<CreateUserDialogComponent>,
    private userCockpitService: UserCockpitService,
    private translocoService: TranslocoService,
  ) { }

  ngOnInit(): void {
  }

  createUser(value: any): void {
    delete value.confirmPassword;
    this.userCockpitService.createUser(value)
      .subscribe((value) => {
        this.closeWithRefresh();
      }) 
  }

  close(): void {
    this.dialog.close();
  }

  closeWithRefresh(): void {
    this.dialog.close(true);
  }

}
