package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

/**
 * TODO Spielecke This type ...
 *
 */
public class AmountOneSlot implements IntentRequestHandler {

  public static String BASE_URL;

  public AmountOneSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menuOne").getValue() != null
        && intentRequest.getIntent().getSlots().get("extraOne").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountOne").getValue() == null;
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.getResponseBuilder().addElicitSlotDirective("amountOne", intentRequest.getIntent())
        .withSpeech("Wie oft möchten Sie, dass von Ihnen ausgewählte Menu bestellen ?").withReprompt("Wie oft ?")
        .build();
  }

}
