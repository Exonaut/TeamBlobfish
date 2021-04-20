import { BookingInfo, ReservationInfo } from 'app/shared/backend-models/interfaces';

export const InvitationDialogComponentStub = {
  data: {
    bookingDate: new Date(),
    email: 'test@gmail.com',
    name: 'test',
    booking: {
      bookingDate: 'Date',
      email: 'test@gmail.com',
      name: 'test',
      bookingType: 1,
      assistants: 1,
    } as ReservationInfo,
    invitedGuests: {
      [0] : { email: 'test@gmail.com'},
      [1] : { email: 'test123@gmail.com'},
    },
  } as BookingInfo,
  invite: {
    modificationCounter: 0,
    id: 1000003,
    name: 'Test34',
    bookingToken: 'CB_20200520_1ae48a790a4ecda291499e7782377732',
    comment: null,
    bookingDate: 1591792123.0,
    expirationDate: 1591788523.0,
    creationDate: 1589977782.4627239,
    email: 'chillalkar@gmail.com',
    canceled: false,
    bookingType: 'INVITED',
    tableId: null,
    orderId: null,
    assistants: null,
    userId: null,
  },
};
