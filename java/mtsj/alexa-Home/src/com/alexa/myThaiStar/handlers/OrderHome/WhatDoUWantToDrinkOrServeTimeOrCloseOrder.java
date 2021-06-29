package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.attributes.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.BasicOperations;

public class WhatDoUWantToDrinkOrServeTimeOrCloseOrder implements IntentRequestHandler {
  /**
   *
   * If the customer wants to drink something. He can also specify a serving time or finish the order in case of
   * delivery
   *
   */
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

    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");
    Slot yesNoDrink = intentRequest.getIntent().getSlots().get("yesNoDrink");
    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (yesNoDrink.getValue().equals("ja")) {

      attributes.replace(Attributes.STATE_KEY_MENU, Attributes.START_STATE_MENU_DRINK);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr erstes Getränk?")
          .withReprompt(
              "Wenn Sie sich noch nicht sicher sind, was sie zum trinken wollen, dann verlangen Sie einfach nach der Getränkekarte.")
          .withShouldEndSession(false).build();
    }

    if (attributes.containsValue(Attributes.START_STATE_WHERE_LIKE_TO_EAT_DELIVER)
        || (whereLikeToEat.getValue() != null && whereLikeToEat.getValue().equals("liefern"))) {

      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();
    }

    if (yesNoDrink.getValue().equals("nein")) {

      String bookingDateTime = BasicOperations
          .convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
      String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
      String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

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
