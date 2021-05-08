package com.tools;

import com.entity.menu.ResponseDescriptionBeer;
import com.google.gson.Gson;

public class Test {

  public static String BASE_URL = "https://250133c949e4.ngrok.io";

  public static void main(String[] args) {

    String speechText = "";
    String payload = "{\"categories\":[{\"id\":\"8\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    BasicOperations bo = new BasicOperations();

    String resStr = "nischt";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
    }

    Gson gson = new Gson();

    ResponseDescriptionBeer response = gson.fromJson(resStr, ResponseDescriptionBeer.class);

    speechText = response.toString();

    System.out.println(response);

  }

}
