package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.BasicOperations;

public class CheckTheCustomerDetails implements IntentRequestHandler {
  /**
   * If details apply to the customer, he can start eating or drinking.
   */
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

    if (yesNoCustomerDetails.getValue().equals("ja"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("eatOrDrink", intentRequest.getIntent())
          .withSpeech("Möchten Sie mit Essen oder Trinken beginnen?")
          .withReprompt("Möchten Sie mit Essen oder Trinken beginnen?").build();

    if (yesNoCustomerDetails.getValue().equals("nein"))
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    String bookingDateTime = BasicOperations.convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
    String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
    String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Sie haben am " + bookingDate + " um " + bookingTime
            + " Uhr mit" + BasicOperations.req.booking.assistants
            + " Gästen, einen Tisch reserviert. Wollen Sie mit diesen Daten fortfahren?")
        .withReprompt("Wollen Sie mit diesen Daten fortfahren?").build();
  }

}
