import { createAction, props, union } from '@ngrx/store';
import { BookingInfo, ReservationInfo } from 'app/shared/backend-models/interfaces';
import { BookingResponse } from '../../models/booking-response.model';

export const bookTable = createAction(
  '[BookTable] Book table',
  props<{ booking: ReservationInfo }>(),
);

export const bookTableSuccess = createAction(
  '[BookTable] Book table success',
  props<{ bookingResponse: BookingResponse }>(),
);

export const bookTableFail = createAction(
  '[BookTable] Book table Fail',
  props<{ error: Error }>(),
);

export const inviteFriends = createAction(
  '[InviteFriends] Invite friends',
  props<{ booking: BookingInfo }>(),
);

export const inviteFriendsSuccess = createAction(
  '[InviteFriends] Invite friends success',
  props<{ bookingResponse: BookingResponse }>(),
);

export const inviteFriendsFail = createAction(
  '[InviteFriends] Invite friends Fail',
  props<{ error: Error }>(),
);

// action types
const all = union({
  bookTable,
  bookTableSuccess,
  bookTableFail,
  inviteFriends,
  inviteFriendsSuccess,
  inviteFriendsFail,
});
export type BookTableActions = typeof all;
