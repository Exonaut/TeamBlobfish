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
public class AmountTwoSlot implements IntentRequestHandler {

  public static String BASE_URL;

  public AmountTwoSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menuTwo").getValue() != null
        && intentRequest.getIntent().getSlots().get("extraTwo").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountTwo").getValue() == null;
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.getResponseBuilder().addElicitSlotDirective("amountTwo", intentRequest.getIntent())
        .withSpeech("Wie oft möchten Sie, dass von Ihnen ausgewählte Menu bestellen ?").withReprompt("Wie oft ?")
        .build();
  }

}
