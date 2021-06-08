package com.tools;

import com.entity.booking.ResponseBooking;

public class Test {

  public static long flexTime = 1800000;

  public static String BASE_URL = "https://c640d4f8e051.ngrok.io";

  public static void main(String[] args) {

    ResponseBooking resp = HelpClass.getAllBookings();

    HelpClass.bookingIDAvailable(resp, "tony2510@gmx.de");

  }

}
