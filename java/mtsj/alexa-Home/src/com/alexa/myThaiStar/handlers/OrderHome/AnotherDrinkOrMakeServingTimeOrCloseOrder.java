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

/**
 *
 * Choose another drink or choose a dish or specify serving time
 *
 */
public class AnotherDrinkOrMakeServingTimeOrCloseOrder implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("yesNoAnotherDrink").getValue() != null
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot yesNoAnotherDrink = intentRequest.getIntent().getSlots().get("yesNoAnotherDrink");
    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");
    Slot dishOrder = intentRequest.getIntent().getSlots().get("dishOrder");
    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (yesNoAnotherDrink.getValue().equals("ja")) {

      Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amountDrinks").withValue(null).build();
      intentRequest.getIntent().getSlots().put("amountDrinks", updateSlot);
      Slot updateSlot3 = Slot.builder().withName("drink").withValue(null).build();
      intentRequest.getIntent().getSlots().put("drink", updateSlot3);
      Slot updateSlot5 = Slot.builder().withName("yesNoAnotherDrink").withValue(null).build();
      intentRequest.getIntent().getSlots().put("yesNoAnotherDrink", updateSlot5);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Was m??chten Sie noch zum trinken?").withReprompt("Was m??chten Sie trinken?")
          .withShouldEndSession(false).build();
    } else if (yesNoAnotherDrink.getValue().equals("nein") // if no food has been selected yet
        && !attributes.containsValue(Attributes.START_STATE_ORDER_EAT) && dishOrder.getValue() == null) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoEat", intentRequest.getIntent())
          .withSpeech("M??chten Sie etwas zum essen bestellen?").withReprompt("Darf es noch etwas zum essen sein?")
          .build();

    }

    else if (yesNoAnotherDrink.getValue().equals("nein")
        && (attributes.containsValue(Attributes.START_STATE_WHERE_LIKE_TO_EAT_DELIVER)
            || (whereLikeToEat.getValue() != null && whereLikeToEat.getValue().equals("liefern")))) {

      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();
    }

    else if (yesNoAnotherDrink.getValue().equals("nein")) { // specify serving time

      String bookingDateTime = BasicOperations
          .convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
      String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
      String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech("Sie haben am " + bookingDate + " um " + bookingTime
              + " Uhr, einen Tisch reserviert. Sie k??nnen jetzt eine Servierzeit angeben. Welche Servierzeit w??nschen Sie?")
          .withReprompt("Welche Servierzeit w??nschen Sie?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoAnotherDrink", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. M??chten Sie noch etwas zum trinken bestellen?")
        .withReprompt("M??chten Sie noch etwas trinken?").build();

  }

}
