package com.tools;

import java.util.Date;

import com.entity.booking.ResponseBooking;
import com.entity.orderline.RequestOrder;

public class Test {

  public static long flexTime = 1800000;

  public static String BASE_URL = "https://7e22d80edae1.ngrok.io";

  public static void main(String[] args) {

    ResponseBooking r = HelpClass.getAllBookings();

    long flexTime = 1800000;

    int tableid = 7;
    Date date = new Date();
    long timeNow = date.getTime();

    for (com.entity.booking.Content c : r.content) {

      long BookingTimeLong = Long.parseLong(c.booking.bookingDate.substring(0, 10) + "") * 1000;

      long diff = BookingTimeLong - timeNow;

      if (Integer.parseInt(c.booking.tableId) == tableid && Math.abs(diff) <= flexTime) {

        HelpClass.req = new RequestOrder();
        HelpClass.req.booking.bookingToken = c.booking.bookingToken;
        HelpClass.req.booking.name = c.booking.name;
        HelpClass.req.booking.assistants = c.booking.assistants;
        HelpClass.req.booking.email = c.booking.email;

        System.out.println(c.booking);

      }

    }

  }

}
