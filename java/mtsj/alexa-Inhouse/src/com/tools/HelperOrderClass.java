package com.tools;

import java.util.ArrayList;

import com.alexa.myThaiStar.MyThaiStarStreamHandler;
import com.entity.dish.Content;
import com.entity.dish.ResponseDescriptionDishes;
import com.entity.dish.ResponseMenuDishes;
import com.entity.orderline.Extras;
import com.entity.orderline.OrderLines;
import com.entity.orderline.RequestOrder;
import com.google.gson.Gson;

public class HelperOrderClass {

  public static String BASE_URL = MyThaiStarStreamHandler.BASE_URL;

  public static RequestOrder req;

  public static ArrayList<Extras> extras;

  public static String getExtrasName(String dishID) {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();
    String resStr = "";

    try {
      resStr = bo.basicGET(BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/" + dishID + "/");
    } catch (Exception ex) {
      String speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
      return speechText;
    }

    OrderLines response = gson.fromJson(resStr, OrderLines.class);

    String extraStr = "Zu diesem Gericht können Sie ";

    extras = response.extras;

    for (int i = 0; i < extras.size(); i++) {

      if (i < response.extras.size() - 1)
        extraStr += response.extras.get(i).name + " für "
            + response.extras.get(i).price.replaceAll("0", "").replace(".", " Euro") + " und oder ";
      else
        extraStr += response.extras.get(i).name + " für "
            + response.extras.get(i).price.replaceAll("0", "").replace(".", " Euro") + " wählen.";

    }

    return extraStr;

  }

  public static ArrayList<String> getExtrasNameArray() {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();
    String resStr = "";

    String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      return null;
    }

    ResponseMenuDishes response = gson.fromJson(resStr, ResponseMenuDishes.class);

    ArrayList<String> extraArray = new ArrayList<>();

    for (Content c : response.content) {
      for (Extras e : c.extras) {

        boolean found = false;
        for (String s : extraArray) {

          if (e.name.equals(s))
            found = true;

        }

        if (!found)
          extraArray.add(e.name);

      }
    }

    return extraArray;

  }

  public static String sendOrder() {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();

    String payload = gson.toJson(req);

    String resp = "";
    try {
      resp = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/ordermanagement/v1/order");
    } catch (Exception ex) {
      String speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt "
          + payload;
      return speechText;
    }

    return "Vielen Dank. Ihre Bestellung wurde aufgenommen";

  }

  public static String getExtrasID(String extraName) {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();
    String resStr = "";

    String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      String speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
      return speechText;
    }

    ResponseMenuDishes response = gson.fromJson(resStr, ResponseMenuDishes.class);

    String extraID = "";

    for (Content c : response.content) {
      for (Extras e : c.extras) {
        if (e.name.toLowerCase().equals(extraName)) {
          extraID = e.id;
          return extraID;
        }
      }
    }

    return null;

  }

  public static String getDishId(String dishName) {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();
    String resStr = "";

    String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      return "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
    }

    ResponseDescriptionDishes response = gson.fromJson(resStr, ResponseDescriptionDishes.class);

    for (int i = 0; i < response.content.length; i++) {
      if (response.content[i].dish.name.toLowerCase().equals(dishName)) {
        return response.content[i].dish.id;
      }
    }
    return null;
  }

}
