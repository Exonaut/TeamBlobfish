package com.tools;

import com.alexa.myThaiStar.handlers.Order.DataFromDatabase;
import com.entity.dish.ResponseDescriptionDishes;
import com.entity.orderline.RequestOrder;
import com.google.gson.Gson;

public class Test {

  public static String BASE_URL = "https://7b763e7f5e9e.ngrok.io";

  public static RequestOrder req = new RequestOrder();

  public static void main(String[] args) {

    System.out.println(DataFromDatabase.getExtrasID("tofu"));

  }

  public static String getDishId(String dishName) {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();
    String resStr = "";

    String payload = "{\"categories\":[{\"id\":\"0\"},{\"id\":\"1\"},{\"id\":\"2\"},{\"id\":\"3\"},{\"id\":\"4\"},{\"id\":\"5\"},{\"id\":\"6\"},{\"id\":\"7\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}\r\n";

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
