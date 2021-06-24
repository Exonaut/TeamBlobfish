package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class EatOrDrink implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && !intentRequest.getIntent().getSlots().get("queryTable").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("eatOrDrink").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot queryTable = intentRequest.getIntent().getSlots().get("queryTable");

    if (queryTable.getConfirmationStatusAsString().equals("DENIED"))
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("eatOrDrink", intentRequest.getIntent())
        .withSpeech("Möchten Sie mit Essen oder Trinken beginnen?")
        .withReprompt("Möchten Sie mit Essen oder Trinken beginnen?").build();
  }

}
