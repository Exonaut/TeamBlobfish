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
      serveTime: '2021-06-28T13:24:43Z',
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
      bookingDate: '2021-06-28T13:24:43Z',
      creationDate: '2021-06-28T13:24:43Z',
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

export const orderDataArchive: OrderListView[] = [
  {
    order: {
      modificationCounter: 1,
      id: 1,
      bookingId: 1,
      invitedGuestId: null,
      bookingToken: null,
      hostId: 1,
      serveTime: '2021-06-28T13:24:43Z',
      revision: 1,
      orderStatus: 6,
      paymentStatus: 0,
      city: '',
      street: '',
      streetNr: '',
    },
    booking: {
      id: 1,
      name: 'user1',
      bookingToken: 'CB_20170509_123567890',
      bookingDate: '2021-06-28T13:24:43Z',
      creationDate: '2021-06-28T13:24:43Z',
      email: 'user1@mail.com',
      canceled: false,
      bookingType: 'COMMON',
      tableId: 1,
      assistants: 4,
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
  }
];

export const orderDataArchiveResponse: any = {
  content: orderDataArchive,
  totalElements: 1,
};
