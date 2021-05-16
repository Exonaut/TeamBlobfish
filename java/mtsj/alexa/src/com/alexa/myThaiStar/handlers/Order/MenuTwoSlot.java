package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.entity.orderline.RequestOrder;

public class MenuTwoSlot implements IntentRequestHandler {

  public static String BASE_URL;

  public static RequestOrder req = new RequestOrder();

  public MenuTwoSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menuTwo").getValue() == null
        && intentRequest.getIntent().getSlots().get("yesNoOne").getConfirmationStatusAsString().equals("CONFIRMED");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.getResponseBuilder().addElicitSlotDirective("menuTwo", intentRequest.getIntent())
        .withSpeech("Was möchten Sie noch zum essen bestellen?").withReprompt("Was darf es sein?").build();

  }

}
