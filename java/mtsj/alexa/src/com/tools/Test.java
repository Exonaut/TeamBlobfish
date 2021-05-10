package com.tools;

import com.entity.menu.ResponseDescriptionDishes;
import com.google.gson.Gson;

public class Test {

  public static String BASE_URL = "https://277dbd81918c.ngrok.io";

  public static void main(String[] args) {

    String speechText = "";
    String payload = "{\"categories\":[{\"id\":\"0\"},{\"id\":\"1\"},{\"id\":\"2\"},{\"id\":\"3\"},{\"id\":\"4\"},{\"id\":\"5\"},{\"id\":\"6\"},{\"id\":\"7\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    BasicOperations bo = new BasicOperations();

    String resStr = "nischt";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
    }

    Gson gson = new Gson();

    ResponseDescriptionDishes response = gson.fromJson(resStr, ResponseDescriptionDishes.class);

    speechText = response.getDishesDescription("thai green chicken curry");

    System.out.println(speechText);

  }

}
