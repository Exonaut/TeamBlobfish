package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.orderline.RequestOrder;

public class ExtraThreeSlot implements IntentRequestHandler {

  public static String BASE_URL;

  public static RequestOrder req = new RequestOrder();

  public ExtraThreeSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menuThree").getValue() != null
        && intentRequest.getIntent().getSlots().get("extraThree").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menuThree = intentRequest.getIntent().getSlots().get("menuThree");

    String dishId = DataFromDatabase.getDishId(menuThree.getValue());

    return handlerInput.getResponseBuilder().addElicitSlotDirective("extraThree", intentRequest.getIntent())
        .withSpeech(DataFromDatabase.getExtras(dishId)).withReprompt("Welche Extras möchten Sie Sie?").build();

  }

}
