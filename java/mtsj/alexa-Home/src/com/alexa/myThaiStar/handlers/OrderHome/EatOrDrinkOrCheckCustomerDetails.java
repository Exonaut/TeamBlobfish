package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.model.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.booking.Booking;
import com.entity.booking.ResponseBooking;
import com.entity.orderline.RequestOrder;
import com.google.gson.Gson;
import com.tools.BasicOperations;

public class EatOrDrinkOrCheckCustomerDetails implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("whereLikeToEat").getValue() != null
        && intentRequest.getIntent().getSlots().get("yesNoCustomerDetails").getValue() == null
        && intentRequest.getIntent().getSlots().get("eatOrDrink").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");

    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (whereLikeToEat.getValue().equals("restaurant")) {

      attributes = handlerInput.getAttributesManager().getSessionAttributes();
      attributes.put(Attributes.STATE_KEY_WHERE_LIKE_TO_EAT, Attributes.START_STATE_WHERE_LIKE_TO_EAT_RESTAURANT);

      ResponseBooking response = BasicOperations.getAllBookingsAndOrders();

      if (response == null)
        return handlerInput.getResponseBuilder()
            .withSpeech("Es ist ein Problem aufgetreten. Bitte versuchen Sie es zu einem späteren Zeitpunkt.").build();

      String userEmail = handlerInput.getServiceClientFactory().getUpsService().getProfileEmail();
      BasicOperations.counterBookingIDs = BasicOperations.bookingIDAvailable(response, userEmail);

      if (BasicOperations.counterBookingIDs == 0)
        return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

      if (BasicOperations.counterBookingIDs == 1) {
        String bookingDateTime = BasicOperations
            .convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
        String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
        String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

        String assistant = " Gästen";
        if (Integer.parseInt(BasicOperations.req.booking.assistants) == 1)
          assistant = " Gast";

        return handlerInput.getResponseBuilder()
            .addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
            .withSpeech("Sie haben am " + bookingDate + " um " + bookingTime + " Uhr mit "
                + BasicOperations.req.booking.assistants + assistant
                + ", einen Tisch reserviert. Wollen Sie mit diesen Daten fortfahren?")
            .withReprompt("Wollen Sie mit diese Daten fortfahren?").build();
      }

      if (BasicOperations.counterBookingIDs > 1) {
        String bookingDateTime = BasicOperations
            .convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
        String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
        String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

        String assistant = " Gästen";
        if (Integer.parseInt(BasicOperations.req.booking.assistants) == 1)
          assistant = " Gast";

        return handlerInput.getResponseBuilder()
            .addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
            .withSpeech("Ich habe mehrere Einträge gefunden. Sie haben am " + bookingDate + " um " + bookingTime
                + " Uhr mit " + BasicOperations.req.booking.assistants + assistant
                + ", einen Tisch reserviert. Wollen Sie mit diesen Daten fortfahren?")
            .withReprompt("Wollen Sie mit diesen Daten fortfahren?").build();
      }

    }

    if (whereLikeToEat.getValue().equals("liefern"))

    {

      attributes = handlerInput.getAttributesManager().getSessionAttributes();
      attributes.put(Attributes.STATE_KEY_WHERE_LIKE_TO_EAT, Attributes.START_STATE_WHERE_LIKE_TO_EAT_DELIVER);

      String personCount = "";
      String name = handlerInput.getServiceClientFactory().getUpsService().getProfileName();
      String userEmail = handlerInput.getServiceClientFactory().getUpsService().getProfileEmail();

      Date date = new Date();
      long timeNow = date.getTime();
      String date_time = BasicOperations.convertMillisecondsToDateTime(timeNow + 600000);
      String response = BasicOperations.bookATable(userEmail, name, date_time, personCount, "2");

      if (response == null) {
        return handlerInput.getResponseBuilder()
            .withSpeech(
                "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
            .build();
      }

      Gson gson = new Gson();
      Booking responseBooking = gson.fromJson(response, Booking.class);

      BasicOperations.req = new RequestOrder();
      // Def. Adr
      BasicOperations.req.order.city = "Bad Belzig";
      BasicOperations.req.order.street = "Am Kurpark";
      BasicOperations.req.order.streetNr = "1A";

      BasicOperations.req.booking.bookingToken = responseBooking.bookingToken;

      return handlerInput.getResponseBuilder().addElicitSlotDirective("eatOrDrink", intentRequest.getIntent())
          .withSpeech("Möchten Sie mit Essen oder Trinken beginnen?")
          .withReprompt("Möchten Sie mit Essen oder Trinken beginnen?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("whereLikeToEat", intentRequest.getIntent())
        .withSpeech(
            " Ich habe Sie leider nicht verstanden. Wollen Sie sich die Bestellung liefern lassen oder ist die Bestellung für einen reservierten Tisch im Restaurant? Sie können sagen im Restaurant oder liefern lassen")
        .withReprompt("Wo möchten Sie essen?").build();
  }

}
