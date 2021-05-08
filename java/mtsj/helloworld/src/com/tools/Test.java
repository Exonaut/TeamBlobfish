package com.tools;

import com.entity.booking.PayloadBookTable;
import com.entity.booking.Request;
import com.google.gson.Gson;

/**
 * TODO Spielecke This type ...
 *
 */
public class Test {

  public static void main(String[] args) {

    com.entity.booking.Request myApiRequest = new Request();
    myApiRequest.booking = new PayloadBookTable();
    myApiRequest.booking.email = "test@web.de";
    myApiRequest.booking.assistants = "" + 6;
    myApiRequest.booking.bookingDate = "heute";
    myApiRequest.booking.name = "Tony";

    Gson gson = new Gson();
    String payload = gson.toJson(myApiRequest);

  }

}
