package com.alexa.myThaiStar.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.dish.ResponseMenuDishes;
import com.entity.dish.ResponseMenuDrinks;
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

    String speechText = "Wählen Sie die Getränkekarte oder Speisekarte";
    String payload = "";

    BasicOperations bo = new BasicOperations();

    com.amazon.ask.model.Request request = input.getRequestEnvelope().getRequest();
    IntentRequest intentRequest = (IntentRequest) request;
    Intent intent = intentRequest.getIntent();
    Map<String, Slot> slots = intent.getSlots();

    Slot menu = slots.get("menu");

    if (menu.getValue().equals("getränkekarte") || menu.getValue().equals("trinken")) {
      payload = "{\"categories\":[{\"id\":\"8\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";
    } else if (menu.getValue().equals("speisekarte") || menu.getValue().equals("essen")) {
      payload = "{\"categories\":[{\"id\":\"0\"},{\"id\":\"1\"},{\"id\":\"2\"},{\"id\":\"3\"},{\"id\":\"4\"},{\"id\":\"5\"},{\"id\":\"6\"},{\"id\":\"7\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";
    }

    String resStr;

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      speechText = "Es tut mir leid. Bitte wiederholen Sie Ihre Angaben";
      return input.getResponseBuilder().withSpeech(speechText + "\n " + payload)
          .withSimpleCard("BookATable", speechText + " \n " + payload).build();
    }

    Gson gson = new Gson();

    if (menu.getValue().equals("getränkekarte") || menu.getValue().equals("trinken")) {

      ResponseMenuDrinks response = gson.fromJson(resStr, ResponseMenuDrinks.class);

      speechText = "Wir haben " + response.toString()
          + ". Wenn Sie mehr über unsere Auswahl zu Tee oder Bier haben möchten, dann sagen sie zum Beispiel: Was gibt es für Teesorten oder was gibt es für Biersorten";

    } else if (menu.getValue().equals("speisekarte") || menu.getValue().equals("essen")) {

      ResponseMenuDishes response = gson.fromJson(resStr, ResponseMenuDishes.class);

      speechText = "Wir haben " + response.toString()
          + " Wenn Sie mehr über die einzelnen Gerichte wissen möchten, dann sagen Sie zum Beispiel: Ich möchte mehr über Thai green chicken curry erfahren. "
          + "Wenn Sie bereits eine Auswahl getroffen haben, dann sagen Sie zum Beispiel: Ich möchte Thai green chicken curry bestellen";

    }

    return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("CallFoodMenu", speechText).build();
  }

}
