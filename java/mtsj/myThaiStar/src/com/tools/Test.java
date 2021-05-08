package com.tools;

import com.entity.menu.ResponseDescriptionFood;
import com.google.gson.Gson;

public class Test {

  public static String BASE_URL = "https://90ee48dabd62.ngrok.io";

  public static void main(String[] args) {

    String speechText = "";
    String payload = "{\"categories\":[{\"id\":\"0\"},{\"id\":\"1\"},{\"id\":\"2\"},{\"id\":\"3\"},{\"id\":\"4\"},{\"id\":\"5\"},{\"id\":\"6\"},{\"id\":\"7\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}\r\n";

    BasicOperations bo = new BasicOperations();

    String resStr = "nischt";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
    }

    Gson gson = new Gson();

    ResponseDescriptionFood response = gson.fromJson(resStr, ResponseDescriptionFood.class);

    speechText = response.getFoodDescription("thaispicybasilfriedrice");

    System.out.println(speechText);

  }

}
