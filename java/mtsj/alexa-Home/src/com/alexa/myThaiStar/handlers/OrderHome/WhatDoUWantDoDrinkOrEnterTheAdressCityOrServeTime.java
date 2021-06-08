package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelpClass;

public class WhatDoUWantDoDrinkOrEnterTheAdressCityOrServeTime implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("yesNoDrink").getValue() != null
        && intentRequest.getIntent().getSlots().get("drink").getValue() == null
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() == null
        && intentRequest.getIntent().getSlots().get("city").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");
    Slot yesNoDrink = intentRequest.getIntent().getSlots().get("yesNoDrink");

    if (intentRequest.getIntent().getSlots().get("yesNoDrink").getValue().equals("ja"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Welches Getränk möchten Sie?").withReprompt("Welches Getränk möchten Sie?").build();

    if (yesNoDrink.getValue().equals("nein") && whereLikeToEat.getValue().equals("liefern")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("city", intentRequest.getIntent()).withSpeech(
          "Um Ihnen das Essen liefern zu können, benötige ich Ihre Adresse. Wie lautet die Stadt, in der Sie wohnen?")
          .withReprompt("Wie lautet die Stadt, in der Sie wohnen?").build();
    }

    if (yesNoDrink.getValue().equals("nein")) {

      // TODO bookingDateTime vllt nicht in eine statische Konstante??
      String bookingDateTime = HelpClass.convertMillisecondsToDateTime(HelpClass.bookingDateTimeMilliseconds);
      String bookingTime = HelpClass.getTimeFormat(bookingDateTime);
      String bookingDate = HelpClass.getDateFormat(bookingDateTime);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech("Sie haben am " + bookingDate + " um " + bookingTime
              + " Uhr, einen Tisch reserviert. Sie können jetzt eine Servierzeit angeben. Welche Servierzeit wünschen Sie?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();
    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoDrink", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Möchten Sie etwas zum trinken bestellen?")
        .withReprompt("Möchten Sie etwas trinken?").build();

  }

}
