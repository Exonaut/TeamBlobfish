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

public class ExtraTwoSlot implements IntentRequestHandler {

  public static String BASE_URL;

  public static RequestOrder req = new RequestOrder();

  public ExtraTwoSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menuTwo").getValue() != null
        && intentRequest.getIntent().getSlots().get("extraTwo").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menuTwo = intentRequest.getIntent().getSlots().get("menuTwo");

    String dishId = DataFromDatabase.getDishId(menuTwo.getValue());

    return handlerInput.getResponseBuilder().addElicitSlotDirective("extraTwo", intentRequest.getIntent())
        .withSpeech(DataFromDatabase.getExtras(dishId)).withReprompt("Welche Extras möchten Sie Sie?").build();

  }

}
