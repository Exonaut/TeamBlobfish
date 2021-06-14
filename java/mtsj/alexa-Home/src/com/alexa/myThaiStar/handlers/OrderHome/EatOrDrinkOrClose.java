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

public class EatOrDrinkOrClose implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("yesNoCustomerDetails").getValue() != null
        && intentRequest.getIntent().getSlots().get("eatOrDrink").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot yesNoCustomerDetails = intentRequest.getIntent().getSlots().get("yesNoCustomerDetails");
    Slot eatOrDrink = intentRequest.getIntent().getSlots().get("eatOrDrink");

    if (yesNoCustomerDetails.getValue().equals("ja"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("eatOrDrink", intentRequest.getIntent())
          .withSpeech("Möchten Sie mit Essen oder Trinken beginnen?")
          .withReprompt("Möchten Sie mit Essen oder Trinken beginnen?").build();

    if (yesNoCustomerDetails.getValue().equals("nein"))
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    String bookingDateTime = HelpClass.convertMillisecondsToDateTime(HelpClass.bookingDateTimeMilliseconds);
    String bookingTime = HelpClass.getTimeFormat(bookingDateTime);
    String bookingDate = HelpClass.getDateFormat(bookingDateTime);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Sie haben am " + bookingDate + " um " + bookingTime
            + " Uhr mit" + HelpClass.req.booking.assistants
            + " Gästen, einen Tisch reserviert. Wollen Sie mit diesen Daten fortfahren?")
        .withReprompt("Wollen Sie mit diese Daten fortfahren?").build();
  }

}
