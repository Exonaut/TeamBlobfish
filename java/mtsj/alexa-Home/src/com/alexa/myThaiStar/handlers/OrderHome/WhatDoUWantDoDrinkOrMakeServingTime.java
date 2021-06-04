package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelperOrderClass;

public class WhatDoUWantDoDrinkOrMakeServingTime implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("yesNoDrink").getValue() != null
        && intentRequest.getIntent().getSlots().get("drink").getValue() == null
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    String bookingDateTime = HelperOrderClass
        .convertMillisecondsToDateTime(HelperOrderClass.bookingDateTimeMilliseconds);
    String bookingTime = HelperOrderClass.getTimeFormat(bookingDateTime);
    String bookingDate = HelperOrderClass.getDateFormat(bookingDateTime);

    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");
    Slot yesNoDrink = intentRequest.getIntent().getSlots().get("yesNoDrink");

    if (intentRequest.getIntent().getSlots().get("yesNoDrink").getValue().equals("ja"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Welches Getränk möchten Sie?").withReprompt("Welches Getränk möchten Sie?").build();

    if (yesNoDrink.equals("nein") && whereLikeToEat.equals("liefern"))
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    if (yesNoDrink.equals("nein"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech("Sie haben am " + bookingDate + " um " + bookingTime
              + " Uhr, einen Tisch reserviert. Sie können jetzt eine Servierzeit angeben. Welche Servierzeit wünschen Sie?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoDrink", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Möchten Sie etwas zum trinken bestellen?")
        .withReprompt("Möchten Sie etwas trinken?").build();

  }

}
