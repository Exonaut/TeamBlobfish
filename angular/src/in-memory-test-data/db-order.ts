import { OrderListView } from 'app/shared/view-models/interfaces';

export const orderData: OrderListView[] = [
  {
    order: {
      modificationCounter: 1,
      id: 0,
      bookingId: 0,
      invitedGuestId: null,
      bookingToken: null,
      hostId: 0,
      serveTime: 'Date',
      revision: 1,
      orderStatus: 0,
      paymentStatus: 0,
      city: '',
      street: '',
      streetNr: '',
    },
    booking: {
      id: 0,
      name: 'user0',
      bookingToken: 'CB_20170509_123502555Z',
      bookingDate: 'Date',
      creationDate: 'Date',
      email: 'user0@mail.com',
      canceled: false,
      bookingType: 'COMMON',
      tableId: 0,
      assistants: 3,
      status: 0,
    },
    orderLines: [
      {
        orderLine: {
          amount: 2,
          comment: 'please not too spicy',
        },
        dish: {
          id: 0,
          name: 'Thai Spicy Basil Fried Rice',
          price: 12.99,
        },
        extras: [
          {
            id: 1,
            name: 'Extra curry',
            price: 1.0,
          },
        ],
      },
      {
        orderLine: {
          amount: 1,
          comment: null,
        },
        dish: {
          id: 4,
          name: 'Thai Thighs Fish/Prawns',
          price: 8.99,
        },
        extras: [
          {
            id: 1,
            name: 'Extra curry',
            price: 1.0,
          },
        ],
      },
      {
        orderLine: {
          amount: 1,
          comment: null,
        },
        dish: {
          id: 2,
          name: 'Thai green chicken curry',
          price: 14.75,
        },
        extras: [
          {
            id: 0,
            name: 'Tofu',
            price: 1.0,
          },
          {
            id: 1,
            name: 'Extra curry',
            price: 1.0,
          },
        ],
      },
    ],
  },
];

export const orderDataResponse: any = {
  content: orderData,
  totalElements: 1,
};
