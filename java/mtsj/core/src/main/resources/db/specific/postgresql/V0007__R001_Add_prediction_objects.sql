CREATE TABLE DateInfo(
  "DATE" DATE,
  TEMPERATURE NUMERIC,
  DESIGNATION VARCHAR(255)
);

CREATE VIEW DailyOrderedDishes AS SELECT
  "ORDERLINE".idDish AS "IDDISH",
  "BOOKING".bookingDate AS "BOOKINGDATE",
  CAST(SUM("ORDERLINE".amount) AS INTEGER) AS "AMOUNT"
FROM OrderLine AS "ORDERLINE"
JOIN Orders AS "ORDERS" ON "ORDERLINE".idOrder = "ORDERS".id
JOIN Booking AS "BOOKING" ON "BOOKING".id = "ORDERS".idBooking
GROUP BY "BOOKINGDATE", "IDDISH"
ORDER BY "BOOKINGDATE";

CREATE VIEW MonthlyOrderedDishes AS SELECT
  "ORDERLINE".idDish AS "IDDISH",
  TO_DATE(TO_CHAR("BOOKING".bookingDate, 'yyyy-MM'), 'yyyy-MM') AS "BOOKINGDATE",
  ROUND(AVG("DATEINFO".TEMPERATURE),1) AS "TEMPERATURE",
  CAST(SUM("ORDERLINE".amount) AS INTEGER) AS "AMOUNT"
FROM OrderLine AS "ORDERLINE"
JOIN Orders AS "ORDERS" ON "ORDERLINE".idOrder = "ORDERS".id
JOIN Booking AS "BOOKING" ON "BOOKING".id = "ORDERS".idBooking
JOIN DateInfo AS "DATEINFO" ON TO_CHAR("DATEINFO"."DATE", 'yyyy-MM') = TO_CHAR("BOOKING".bookingDate, 'yyyy-MM')
GROUP BY TO_DATE(TO_CHAR("BOOKING".bookingDate, 'yyyy-MM'), 'yyyy-MM'), "IDDISH"
ORDER BY "BOOKINGDATE";

CREATE VIEW OrderedDishesPerDay AS SELECT
  CAST(TO_CHAR("DAILYORDEREDDISHES"."BOOKINGDATE", 'yyyyMMdd') || "DAILYORDEREDDISHES"."IDDISH" AS BIGINT) AS "ID",
  "DISH".modificationCounter,
  "DATEINFO".TEMPERATURE,
  "DATEINFO".DESIGNATION,
  "DAILYORDEREDDISHES".*
FROM DailyOrderedDishes AS "DAILYORDEREDDISHES"
JOIN Dish AS "DISH" ON "DAILYORDEREDDISHES"."IDDISH" = "DISH".id
JOIN DateInfo AS "DATEINFO" ON TO_CHAR("DATEINFO"."DATE", 'yyyy-MM-dd') = TO_CHAR("DAILYORDEREDDISHES"."BOOKINGDATE", 'yyyy-MM-dd');

CREATE VIEW OrderedDishesPerMonth AS SELECT
  CAST(TO_CHAR("MONTHLYORDEREDDISHES"."BOOKINGDATE", 'yyyyMMdd') || "MONTHLYORDEREDDISHES"."IDDISH" AS BIGINT) AS "ID",
  "DISH".modificationCounter,
  "MONTHLYORDEREDDISHES".*
FROM MonthlyOrderedDishes AS "MONTHLYORDEREDDISHES"
JOIN Dish AS "DISH" ON "MONTHLYORDEREDDISHES"."IDDISH" = "DISH".id;

CREATE TABLE PREDICTION_ALL_FORECAST (
  id BIGSERIAL NOT NULL,
  modificationCounter INTEGER NOT NULL,
  idDish BIGINT NOT NULL,
  dishName VARCHAR(255),
  timestamp INTEGER,
  forecast NUMERIC,
  CONSTRAINT PK_PREDICTION_ALL_FORECAST PRIMARY KEY(id)
);

CREATE TABLE PREDICTION_ALL_MODELS (
  id BIGSERIAL NOT NULL,
  modificationCounter INTEGER NOT NULL,
  idDish BIGINT NOT NULL,
  "KEY" VARCHAR(100),
  "VALUE" VARCHAR(5000),
  CONSTRAINT PK_PREDICTION_ALL_MODELS PRIMARY KEY(id)
);

CREATE TABLE PREDICTION_FORECAST_DATA (
  "TIMESTAMP" INTEGER,
  "TEMPERATURE" NUMERIC,
  "HOLIDAY" INTEGER,
  CONSTRAINT PK_PREDICTION_FORECAST_DATA PRIMARY KEY("TIMESTAMP")
);

