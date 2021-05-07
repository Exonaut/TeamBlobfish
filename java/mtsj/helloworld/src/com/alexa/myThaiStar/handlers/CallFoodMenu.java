package com.alexa.myThaiStar.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.entity.menu.ResponseMenu;
import com.google.gson.Gson;
import com.tools.BasicOperations;

public class CallFoodMenu implements RequestHandler {

  private static String BASE_URL;

  /**
   * The constructor.
   *
   * @param baseUrl
   */
  public CallFoodMenu(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput input) {

    return input.matches(intentName("callMenu"));
  }

  @Override
  public Optional<Response> handle(HandlerInput input) {

    String speechText = "funktioniert";

    BasicOperations bo = new BasicOperations();

    String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    String resStr;

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
      return input.getResponseBuilder().withSpeech(speechText + "\n " + payload)
          .withSimpleCard("BookATable", speechText + " \n " + payload).build();
    }

    Gson gson = new Gson();

    ResponseMenu response = gson.fromJson(resStr, ResponseMenu.class);

    speechText = response.toString();

    return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("CallFoodMenu", speechText)
        .withReprompt("Test").build();
  }

}
