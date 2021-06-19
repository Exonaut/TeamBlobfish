package com.alexa.myThaiStar.handlers.orderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.model.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

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

    Slot yesNoDrink = intentRequest.getIntent().getSlots().get("yesNoDrink");
    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (yesNoDrink.getValue().equals("ja")) {

      attributes.replace(Attributes.STATE_KEY_MENU, Attributes.START_STATE_MENU_DRINK);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent()).withSpeech(
          "Wie lautet Ihr erstes Getränk? Wenn Sie sich noch nicht sicher sind, was sie zum trinken wollen, dann verlangen Sie einfach nach der Getränkekarte.")
          .withReprompt("Was möchten Sie trinken?").withShouldEndSession(false).build();

    } else if (yesNoDrink.getValue().equals("nein")) {

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
