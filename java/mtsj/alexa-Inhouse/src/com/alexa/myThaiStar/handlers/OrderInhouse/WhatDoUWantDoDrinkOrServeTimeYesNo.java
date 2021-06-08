package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

public class WhatDoUWantDoDrinkOrServeTimeYesNo implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("yesNoDrink").getValue() != null
        && intentRequest.getIntent().getSlots().get("drink").getValue() == null
        && intentRequest.getIntent().getSlots().get("serveTimeYesNo").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    if (intentRequest.getIntent().getSlots().get("yesNoDrink").getValue().equals("ja")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Welches Getränk möchten Sie?").withReprompt("Welches Getränk möchten Sie?").build();

    } else if (intentRequest.getIntent().getSlots().get("yesNoDrink").getValue().equals("nein")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("serveTimeYesNo", intentRequest.getIntent())
          .withSpeech(
              "Sie haben die Möglichkeit, eine Servierzeit anzugeben. Wenn Sie keine Servierzeit angeben möchten, "
                  + "wird Ihnen Ihr Essen in 30 minuten serviert. Wenn Sie eine Servierzeit angeben möchten, "
                  + "dann muss die Servierzeit mindestens 30 minuten hinter der aktuellen Zeit liegen. "
                  + "Möchten Sie eine Servierzeit angeben?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoDrink", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Möchten Sie etwas zum trinken bestellen?")
        .withReprompt("Möchten Sie etwas trinken?").build();

  }

}
