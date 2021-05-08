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
import com.entity.menu.ResponseDescriptionBeer;
import com.entity.menu.ResponseDescriptionTee;
import com.google.gson.Gson;
import com.tools.BasicOperations;

public class DescriptionDrinks implements RequestHandler {

  private static String BASE_URL;

  /**
   * The constructor.
   *
   * @param baseUrl
   */
  public DescriptionDrinks(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput input) {

    return input.matches(intentName("descriptionDrinks"));
  }

  @Override
  public Optional<Response> handle(HandlerInput input) {

    com.amazon.ask.model.Request request = input.getRequestEnvelope().getRequest();
    IntentRequest intentRequest = (IntentRequest) request;
    Intent intent = intentRequest.getIntent();
    Map<String, Slot> slots = intent.getSlots();

    Slot drinks = slots.get("drinks");

    String speechText = "Tut mir leid, das haben wir leider nicht im Angebot";
    String payload = "{\"categories\":[{\"id\":\"8\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    BasicOperations bo = new BasicOperations();

    String resStr;

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
      return input.getResponseBuilder().withSpeech(speechText + "\n " + payload)
          .withSimpleCard("BookATable", speechText + " \n " + payload).build();
    }

    Gson gson = new Gson();

    if (drinks.getValue().equals("tee")) {

      ResponseDescriptionTee response = gson.fromJson(resStr, ResponseDescriptionTee.class);
      speechText = response.toString();

    } else if (drinks.getValue().equals("bier")) {
      ResponseDescriptionBeer response = gson.fromJson(resStr, ResponseDescriptionBeer.class);
      speechText = response.toString();
    }

    return input.getResponseBuilder().withSpeech("Wir haben " + speechText)
        .withSimpleCard("descriptionDrinks", speechText).build();
  }

}
