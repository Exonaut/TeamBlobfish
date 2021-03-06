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

public class AnotherDishOrDoUWantToDrink implements IntentRequestHandler {

  /**
   *
   * Choose another dish or choose a drink. If both have already been selected then you can specify a serving time or
   * cancel the order in case of a delivery
   *
   */

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() != null
        && intentRequest.getIntent().getSlots().get("yesNoDrink").getValue() == null
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot yesNoEat = intentRequest.getIntent().getSlots().get("yesNoEat");
    Slot drink = intentRequest.getIntent().getSlots().get("drink");
    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");
    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    // if further dish then set the values to null
    if (yesNoEat.getValue().equals("ja")) {

      Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amount").withValue(null).build();
      intentRequest.getIntent().getSlots().put("amount", updateSlot);
      Slot updateSlot2 = Slot.builder().withName("extra").withValue(null).build();
      intentRequest.getIntent().getSlots().put("extra", updateSlot2);
      Slot updateSlot3 = Slot.builder().withName("dishOrder").withValue(null).build();
      intentRequest.getIntent().getSlots().put("dishOrder", updateSlot3);
      Slot updateSlot4 = Slot.builder().withName("yesNoEat").withValue(null).build();
      intentRequest.getIntent().getSlots().put("yesNoEat", updateSlot4);

      attributes.replace(Attributes.STATE_KEY_MENU, Attributes.START_STATE_MENU_EAT);

      if (drink.getValue() != null) {
        return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
            .withSpeech(
                "Wie lautet Ihr erstes Gericht? Wenn Sie sich noch nicht sicher sind, was sie zum essen wollen, dann verlangen Sie einfach nach der Speisekarte.")
            .withReprompt("Was m??chten Sie essen?").withShouldEndSession(false).build();
      }
      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr weiteres Gericht?").withReprompt("Was m??chten Sie essen?").build();
    } else if (yesNoEat.getValue().equals("nein")
        && (drink.getValue() != null || attributes.containsValue(Attributes.START_STATE_ORDER_DRINK))
        && (attributes.containsValue(Attributes.START_STATE_WHERE_LIKE_TO_EAT_RESTAURANT)
            || (whereLikeToEat.getValue() != null && whereLikeToEat.getValue().equals("restaurant")))) {

      String bookingDateTime = BasicOperations
          .convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
      String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
      String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent()) // specify
                                                                                                                // a
                                                                                                                // serving
                                                                                                                // time
          .withSpeech("Sie haben am " + bookingDate + " um " + bookingTime
              + " Uhr, einen Tisch reserviert. Sie k??nnen jetzt eine Servierzeit angeben. Welche Servierzeit w??nschen Sie?")
          .withReprompt("Welche Servierzeit w??nschen Sie?").build();

    } else if (yesNoEat.getValue().equals("nein")
        && (drink.getValue() != null || attributes.containsValue(Attributes.START_STATE_ORDER_DRINK))
        && (attributes.containsValue(Attributes.START_STATE_WHERE_LIKE_TO_EAT_DELIVER)
            || (whereLikeToEat.getValue() != null && whereLikeToEat.getValue().equals("liefern")))) {
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();
    }

    else if (yesNoEat.getValue().equals("nein")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoDrink", intentRequest.getIntent())
          .withSpeech("M??chten Sie etwas zum trinken bestellen?").withReprompt("M??chten Sie etwas trinken?").build();
    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoEat", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. M??chten Sie noch etwas zum essen bestellen?")
        .withReprompt("M??chten Sie noch etwas essen?").build();

  }

}
