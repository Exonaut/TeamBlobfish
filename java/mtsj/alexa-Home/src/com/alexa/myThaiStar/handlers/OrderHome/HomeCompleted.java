package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.tools.HelperOrderClass;

public class HomeCompleted implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.COMPLETED);
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    if (intentRequest.getIntent().getSlots().get("dishOrder").getValue() == null)
      return handlerInput.getResponseBuilder()
          .withSpeech(
              "Keine Buchungs ID gefunden. Bitte buchen Sie zuerst einen Tisch, bevor Sie eine Bestellung vornehmen.")
          .build();

    String speechText = HelperOrderClass.sendOrder();

    return handlerInput.getResponseBuilder().withSpeech(speechText).build();
  }

}
