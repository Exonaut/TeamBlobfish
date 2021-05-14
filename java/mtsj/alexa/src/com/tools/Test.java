package com.tools;

import org.apache.http.message.BasicHeader;

import com.entity.booking.Content;
import com.entity.booking.ResponseBooking;
import com.google.gson.Gson;
import com.login.RequestLogin;

public class Test {

  public static String BASE_URL = "https://5befbaea6e41.ngrok.io";

  public static void main(String[] args) {

    String speechText = "";
    String payload = "{\"username\":\"waiter\",\"password\":\"waiter\"}";

    RequestLogin req = new RequestLogin();

    req.password = "waiter";
    req.username = "waiter";
    Gson gson = new Gson();
    payload = gson.toJson(req);

    BasicOperations bo = new BasicOperations();

    String resStr = "nischt";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/login");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
    }

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

    System.out.println(bookingIDAvailable(response));

  }

  public static boolean bookingIDAvailable(ResponseBooking response) {

    for (Content c : response.content) {

      if (c.booking.email.equals("host1@mail.co"))
        return true;

    }

    return false;
  }

}
