package com.tools;

import org.apache.http.message.BasicHeader;

import com.entity.booking.ResponseBooking;
import com.google.gson.Gson;

public class Test {

  public static String BASE_URL = "https://8b148edf102e.ngrok.io";

  public static void main(String[] args) {

    String speechText = "";
    String payload = "{\"username\":\"waiter\",\"password\":\"waiter\"}";

    BasicOperations bo = new BasicOperations();

    String resStr = "nischt";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/login");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
    }

    Gson gson = new Gson();

    String authorizationBearer = bo.getSpecificHeader("Authorization");

    payload = "{\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[]}}";

    BasicOperations bo2 = new BasicOperations();

    bo2.reqHeaders = new BasicHeader[] { new BasicHeader("Authorization", authorizationBearer) };

    try {
      resStr = bo2.basicPost(payload, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking/search");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
    }

    ResponseBooking response = gson.fromJson(resStr, ResponseBooking.class);

    System.out.println(resStr);

  }
}
