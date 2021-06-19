package com.alexa.myThaiStar.handlers.editOrders;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.booking.ResponseBooking;
import com.tools.HelpClass;

public class OrderIsRequested implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("editOrder")) && intentRequest.getDialogState() == DialogState.STARTED);
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    ResponseBooking response = HelpClass.getAllBookingsAndOrders();

    if (response == null)
      return handlerInput.getResponseBuilder()
          .withSpeech("Es ist ein Problem aufgetreten. Bitte versuchen Sie es zu einem späteren Zeitpunkt.").build();

    if (HelpClass.counterBookingIDs == 0)
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    if (HelpClass.counterBookingIDs == 1) {
      String bookingDateTime = HelpClass.convertMillisecondsToDateTime(HelpClass.bookingDateTimeMilliseconds);
      String bookingTime = HelpClass.getTimeFormat(bookingDateTime);
      String bookingDate = HelpClass.getDateFormat(bookingDateTime);

      String assistant = " Gästen";
      if (Integer.parseInt(HelpClass.req.booking.assistants) == 1)
        assistant = " Gast";

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
          .withSpeech(
              "Sie haben für " + bookingDate + " um " + bookingTime + " Uhr mit " + HelpClass.req.booking.assistants
                  + assistant + ", eine Bestellung aufgegeben. Wollen Sie mit diesen Daten fortfahren?")
          .withReprompt("Wollen Sie mit diese Daten fortfahren?").build();
    }

    if (HelpClass.counterBookingIDs > 1) {
      String bookingDateTime = HelpClass.convertMillisecondsToDateTime(HelpClass.bookingDateTimeMilliseconds);
      String bookingTime = HelpClass.getTimeFormat(bookingDateTime);
      String bookingDate = HelpClass.getDateFormat(bookingDateTime);

      String assistant = " Gästen";
      if (Integer.parseInt(HelpClass.req.booking.assistants) == 1)
        assistant = " Gast";

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
          .withSpeech("Ich habe mehrere Einträge gefunden. Sie haben für " + bookingDate + " um " + bookingTime
              + " Uhr mit " + HelpClass.req.booking.assistants + assistant
              + ", eine Bestellung aufgegeben.. Wollen Sie mit diesen Daten fortfahren?")
          .withReprompt("Wollen Sie mit diesen Daten fortfahren?").build();
    }

    Slot eatOrDrink = intentRequest.getIntent().getSlots().get("eatOrDrink");
    Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("eatOrDrink", intentRequest.getIntent())
        .withSpeech("Möchten Sie mit Essen oder Trinken beginnnen?")
        .withReprompt("Möchten Sie mit Essen oder Trinken beginnnen?").build();
  }

}
