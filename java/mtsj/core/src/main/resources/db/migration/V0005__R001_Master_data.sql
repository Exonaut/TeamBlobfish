INSERT INTO UserRole(id, modificationCounter, name, active) VALUES (0, 1, 'Customer', true);
INSERT INTO UserRole(id, modificationCounter, name, active) VALUES (1, 1, 'Waiter', true);
INSERT INTO UserRole(id, modificationCounter, name, active) VALUES (2, 1, 'Manager', true);
INSERT INTO UserRole(id, modificationCounter, name, active) VALUES (3, 1, 'Admin', true);

INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole) VALUES (0, 1, 'user0', '{bcrypt}$2a$10$qPM1WjcRKAffHxWXYEfPJOh2vGPlT/Fdv.hJX/LaZjzg/Wtj2csqO', false, 'user0@mail.com', 0);
INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole) VALUES (1, 1, 'waiter', '{bcrypt}$2a$10$1CAKyUHbX6RJqT5cUP6/aOMTIlYYvGIO/a3Dt/erbYKKgmbgJMGsG', false, 'waiter@mail.com', 1);
INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole) VALUES (2, 1, 'manager', '{bcrypt}$2a$10$IsTlZemkiPKE2gjtnSMlJOX5.uitNHXNRpLYyvyxNbHEhjpY.XdTq', false, 'manager@mail.com', 2);

INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole) VALUES (3, 1, 'admin', '{bcrypt}$2a$10$8vs9/UFkuXlTF2RvwAykQ.BubvNbpmLyPkqmua.xvByA4LPF/ZEZe', false, 'admin@mail.com', 3);
INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole) VALUES (4, 1, 'sarah','{bcrypt}$2a$10$TxSSs3MiJM6W8cFe8wLBY.fnaOq/ZQqKOA35HYdF8NkIxhcWGyKrC', false, 'user4@mail.com', 0); /* password: test*/
INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole) VALUES (5, 1, 'tom','{bcrypt}$2a$10$TxSSs3MiJM6W8cFe8wLBY.fnaOq/ZQqKOA35HYdF8NkIxhcWGyKrC', false, 'waiter2@mail.com', 1); /* password: test*/
INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole) VALUES (6, 1, 'max','{bcrypt}$2a$10$TxSSs3MiJM6W8cFe8wLBY.fnaOq/ZQqKOA35HYdF8NkIxhcWGyKrC', false, 'waiter3@mail.com', 1); /* password: test*/
INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole) VALUES (7, 1, 'nina','{bcrypt}$2a$10$TxSSs3MiJM6W8cFe8wLBY.fnaOq/ZQqKOA35HYdF8NkIxhcWGyKrC', false, 'admin2@mail.com', 2); /* password: test*/


INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (0, 1, 4);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (1, 1, 4);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (2, 1, 4);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (3, 1, 4);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (4, 1, 6);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (5, 1, 6);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (6, 1, 6);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (7, 1, 8);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (8, 1, 8);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (9, 1, 5);
INSERT INTO "Table"(id, modificationCounter, seatsNumber) VALUES (10, 1, 7);


-- Common Booking with already created orders
INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants) VALUES(0, 1, 0, 'user0', 'CB_20170509_123502555Z', 'Booking Type CSR', 'user0@mail.com', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', 5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 0, 0, 3);
INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants) VALUES(1, 1, 0, 'user1', 'CB_20170510_123502575Z', 'Booking Type GSR', 'user1@mail.com', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', 5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 1, 1, 1, null);
INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants) VALUES(5, 1, 0, 'sarah', 'CB_20170509_123502565Z', 'Booking Type CSR', 'user4@mail.com', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', 5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 9, 0, 5);

-- Common Booking without orders
INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants) VALUES(2, 1, 0, 'user2', 'CB_20170510_123502595Z', 'Booking Type GSR', 'user2@mail.com', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', 5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 2, null, 5);
INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants) VALUES(6, 1, 0, 'user5', 'CB_20170610_123502595Z', 'Booking Type GSR', 'user5@mail.com', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', 5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 2, null, 5);
INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants) VALUES(7, 1, 0, 'user2', 'CB_20170610_123502595Z', 'Booking Type GSR', 'user6@mail.com', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', 5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 2, null, 5);

-- Guest Booking
INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants) VALUES(3, 1, 0, 'host1', 'CB_20170510_123502655Z', 'Booking Type GSR', 'host1@mail.com', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', 5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 1, 3, null, null);
-- guests for booking with id 3
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(0, 1, 3, 'GB_20170510_02350266501Z', 'guest0@mail.com', true, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(1, 1, 3, 'GB_20170510_12350266501Z', 'guest1@mail.com', true, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(2, 1, 3, 'GB_20170510_22350266501Z', 'guest2@mail.com', false, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(3, 1, 3, 'GB_20170510_32350266501Z', 'guest3@mail.com', true, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(4, 1, 3, 'GB_20170510_42350266501Z', 'guest4@mail.com', false, DATEADD('DAY', 5, CURRENT_TIMESTAMP));

---- Guest Booking
INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants) VALUES(4, 1, 0, 'host1', 'CB_20170510_123503600Z', 'Booking Type GSR', 'host1@mail.com', DATEADD('DAY', 5, CURRENT_TIMESTAMP), DATEADD('DAY', 5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 1, 3, null, null);
---- guests for booking with id 4
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(5, 1, 4, 'GB_20170510_52350266501Z', 'guest5@mail.com', true, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(6, 1, 4, 'GB_20170510_62350266501Z', 'guest6@mail.com', false, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(7, 1, 4, 'GB_20170510_72350266501Z', 'guest7@mail.com', true, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(8, 1, 4, 'GB_20170510_82350266501Z', 'guest0@mail.com', true, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate) VALUES(9, 1, 4, 'GB_20170510_92350266501Z', 'guest1@mail.com', false, DATEADD('DAY', 5, CURRENT_TIMESTAMP));
