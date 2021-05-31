package com.tools;

import java.io.IOException;
import java.util.Date;

import org.apache.http.message.BasicHeader;

import com.entity.booking.Booking;
import com.entity.booking.Content;
import com.entity.booking.ResponseBooking;
import com.google.gson.Gson;
import com.login.RequestLogin;
import com.tools.exceptions.Different;
import com.tools.exceptions.NotFound;

public class Test {

  public static long flexTime = 1800000;

  public static String BASE_URL = "https://71283d2253d8.ngrok.io";

  public static void main(String[] args) {

    RequestLogin req = new RequestLogin();
    req.password = "waiter";
    req.username = "waiter";
    Gson gson = new Gson();
    String payload = gson.toJson(req);

    BasicOperations bo = new BasicOperations();

    String resp = "";

    try {
      resp = bo.basicPost(payload, BASE_URL + "/mythaistar/login");
    } catch (IOException | NotFound | Different e) {
      System.out.println("Login Hat nicht funktioniert" + "\n" + resp);
    }

    String authorizationBearer = bo.getSpecificHeader("Authorization");

    payload = "{\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[]}}";

    BasicOperations bo2 = new BasicOperations();

    bo2.reqHeaders = new BasicHeader[] { new BasicHeader("Authorization", authorizationBearer) };

    try {
      resp = bo2.basicPost(payload, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking/search");
    } catch (IOException | NotFound | Different e) {
      System.out.println("Die Suche nach Booking hat nicht funktioniert" + "\n" + resp);
    }

    ResponseBooking booking = gson.fromJson(resp, ResponseBooking.class);

    String bookingDate = booking.content[2].booking.bookingDate.replace(".", "");

    System.out.println(bookingDate);

    Date date = new Date();

    long timeNow = date.getTime();

    Booking book = new Booking();

    for (Content c : booking.content) {
      if (Integer.parseInt(c.booking.tableId) == 8
          && Math.abs((Long.parseLong(c.booking.bookingDate.replace(".000000000", "")) * 1000) - timeNow) <= flexTime) {

        long diff = Math.abs((Long.parseLong(c.booking.bookingDate.replace(".000000000", "")) * 1000) - timeNow);

        book.bookingToken = c.booking.bookingToken;
        book.name = c.booking.name;

        System.out.println("Das ist die " + diff);

      }

    }

    System.out.println(book);

  }

}
