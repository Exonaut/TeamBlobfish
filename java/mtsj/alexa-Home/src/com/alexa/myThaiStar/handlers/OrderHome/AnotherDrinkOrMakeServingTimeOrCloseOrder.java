package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelpClass;

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
          .withSpeech("Was möchten Sie noch zum trinken?").withReprompt("Was möchten Sie noch zum trinken?").build();
    } else if (yesNoAnotherDrink.getValue().equals("nein") && !attributes.containsValue("trinken")
        && dishOrder.getValue() == null) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoEat", intentRequest.getIntent())
          .withSpeech("Möchten Sie etwas zum essen bestellen?").withReprompt("Darf es noch etwas zum essen sein?")
          .build();

    }

    else if (yesNoAnotherDrink.getValue().equals("nein")
        && (attributes.containsValue("liefern") || whereLikeToEat.getValue().equals("liefern"))) {

      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();
    }

    else if (yesNoAnotherDrink.getValue().equals("nein")) {

      String bookingDateTime = HelpClass.convertMillisecondsToDateTime(HelpClass.bookingDateTimeMilliseconds);
      String bookingTime = HelpClass.getTimeFormat(bookingDateTime);
      String bookingDate = HelpClass.getDateFormat(bookingDateTime);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech("Sie haben am " + bookingDate + " um " + bookingTime
              + " Uhr, einen Tisch reserviert. Sie können jetzt eine Servierzeit angeben. Welche Servierzeit wünschen Sie?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoAnotherDrink", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Möchten Sie noch etwas zum trinken bestellen?")
        .withReprompt("Möchten Sie noch etwas trinken?").build();

  }

}
