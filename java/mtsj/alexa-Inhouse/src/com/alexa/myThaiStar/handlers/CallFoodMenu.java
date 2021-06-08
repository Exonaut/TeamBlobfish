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
import com.tools.HelpClass;

public class CallFoodMenu implements RequestHandler {

  @Override
  public boolean canHandle(HandlerInput input) {

    return input.matches(intentName("callMenu"));
  }

  @Override
  public Optional<Response> handle(HandlerInput input) {

    String speechText = "Wählen Sie die Getränkekarte oder Speisekarte";
    String payload = "";

    com.amazon.ask.model.Request request = input.getRequestEnvelope().getRequest();
    IntentRequest intentRequest = (IntentRequest) request;
    Intent intent = intentRequest.getIntent();
    Map<String, Slot> slots = intent.getSlots();
    Gson gson = new Gson();
    Slot menu = slots.get("menu");
    String respStr = "";

    if (menu.getValue().equals("getränkekarte") || menu.getValue().equals("trinken")) {
      payload = "{\"categories\":[{\"id\":\"8\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

      respStr = HelpClass.callFoodMenu(payload);

      if (respStr == null)
        return input.getResponseBuilder()
            .withSpeech(
                "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
            .build();

      ResponseMenuDrinks response = gson.fromJson(respStr, ResponseMenuDrinks.class);

      speechText = "Wir haben " + response.toString()
          + ". Wenn Sie mehr über unsere Auswahl zu Tee oder Bier haben möchten, dann sagen sie zum Beispiel: Was gibt es für Teesorten oder was gibt es für Biersorten";

      return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("CallFoodMenu", speechText).build();

    }

    if (menu.getValue().equals("speisekarte") || menu.getValue().equals("essen")) {
      payload = "{\"categories\":[{\"id\":\"0\"},{\"id\":\"1\"},{\"id\":\"2\"},{\"id\":\"3\"},{\"id\":\"4\"},{\"id\":\"5\"},{\"id\":\"6\"},{\"id\":\"7\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

      respStr = HelpClass.callFoodMenu(payload);

      if (respStr == null)
        return input.getResponseBuilder()
            .withSpeech(
                "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
            .build();

      ResponseMenuDishes response = gson.fromJson(respStr, ResponseMenuDishes.class);

      speechText = "Wir haben " + response.toString()
          + ". Wenn Sie mehr über die einzelnen Gerichte wissen möchten, dann sagen Sie zum Beispiel: Ich möchte mehr über Thai green chicken curry erfahren.";

      return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("CallFoodMenu", speechText).build();

    }

    return input.getResponseBuilder()
        .withSpeech("Ich habe Sie leider nicht verstanden. Bitte wiederholen Sie die Anfrage erneut.").build();

  }

}
