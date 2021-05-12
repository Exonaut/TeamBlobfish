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
import com.entity.dish.ResponseDescriptionDishes;
import com.google.gson.Gson;
import com.tools.BasicOperations;

public class DescriptionDishes implements RequestHandler {

  public static String BASE_URL;

  public DescriptionDishes(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput input) {

    return input.matches(intentName("descriptionDish"));
  }

  @Override
  public Optional<Response> handle(HandlerInput input) {

    com.amazon.ask.model.Request request = input.getRequestEnvelope().getRequest();
    IntentRequest intentRequest = (IntentRequest) request;
    Intent intent = intentRequest.getIntent();
    Map<String, Slot> slots = intent.getSlots();

    Slot dishName = slots.get("dishName");

    String speechText = "";
    String payload = "{\"categories\":[{\"id\":\"0\"},{\"id\":\"1\"},{\"id\":\"2\"},{\"id\":\"3\"},{\"id\":\"4\"},{\"id\":\"5\"},{\"id\":\"6\"},{\"id\":\"7\"}],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}\r\n";

    BasicOperations bo = new BasicOperations();

    String resStr;

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      speechText = "Es tut mir leid. Bitte wiederholen Sie Ihre Angaben";
      return input.getResponseBuilder().withSpeech(speechText + "\n " + payload)
          .withSimpleCard("BookATable", speechText + " \n " + payload).build();
    }

    Gson gson = new Gson();

    ResponseDescriptionDishes response = gson.fromJson(resStr, ResponseDescriptionDishes.class);

    speechText = response.getDishesDescription(dishName.getValue());

    return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("descriptionFood", speechText).build();
  }

}
