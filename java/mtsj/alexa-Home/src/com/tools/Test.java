package com.tools;

public class Test {

  public static long flexTime = 1800000;

  public static String BASE_URL = "https://c640d4f8e051.ngrok.io";

  public static void main(String[] args) {

    HelpClass.getAllBookingsAndOrders();
    HelpClass.orderAvailable("tony2510@gmx.de");

    int counterBookingIDs = 0;

    System.out.println(counterBookingIDs);

  }

}
