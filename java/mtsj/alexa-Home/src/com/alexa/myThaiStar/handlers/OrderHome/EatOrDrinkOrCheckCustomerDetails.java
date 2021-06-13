package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Date;
import java.util.Optional;

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
import com.tools.HelpClass;

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

    if (whereLikeToEat.getValue().equals("restaurant")) {

      ResponseBooking response = HelpClass.getAllBookings();

      if (response == null)
        return handlerInput.getResponseBuilder()
            .withSpeech("Es ist ein Problem aufgetreten. Bitte versuchen Sie es zu einem späteren Zeitpunkt.").build();

      String userEmail = handlerInput.getServiceClientFactory().getUpsService().getProfileEmail();
      HelpClass.counterBookingIDs = HelpClass.bookingIDAvailable(response, userEmail);

      if (HelpClass.counterBookingIDs == 0)
        return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

      if (HelpClass.counterBookingIDs == 1) {
        String bookingDateTime = HelpClass.convertMillisecondsToDateTime(HelpClass.bookingDateTimeMilliseconds);
        String bookingTime = HelpClass.getTimeFormat(bookingDateTime);
        String bookingDate = HelpClass.getDateFormat(bookingDateTime);

        return handlerInput.getResponseBuilder()
            .addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
            .withSpeech(
                "Sie haben am " + bookingDate + " um " + bookingTime + " Uhr mit " + HelpClass.req.booking.assistants
                    + " Gästen, einen Tisch reserviert. Wollen Sie mit diesen Daten fortfahren?")
            .withReprompt("Wollen Sie mit diese Daten fortfahren?").build();
      }

      if (HelpClass.counterBookingIDs > 1) {
        String bookingDateTime = HelpClass.convertMillisecondsToDateTime(HelpClass.bookingDateTimeMilliseconds);
        String bookingTime = HelpClass.getTimeFormat(bookingDateTime);
        String bookingDate = HelpClass.getDateFormat(bookingDateTime);

        return handlerInput.getResponseBuilder()
            .addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
            .withSpeech("Ich habe mehrere Einträge gefunden. Sie haben am " + bookingDate + " um " + bookingTime
                + " Uhr mit " + HelpClass.req.booking.assistants
                + " Gästen, einen Tisch reserviert. Wollen Sie mit diese Daten fortfahren?")
            .withReprompt("Wollen Sie mit diese Daten fortfahren?").build();
      }

    }

    if (whereLikeToEat.getValue().equals("liefern")) {

      String personCount = "";
      String name = handlerInput.getServiceClientFactory().getUpsService().getProfileName();
      String userEmail = handlerInput.getServiceClientFactory().getUpsService().getProfileEmail();

      Date date = new Date();
      long timeNow = date.getTime() + 7200000;
      String date_time = HelpClass.convertMillisecondsToDateTime(timeNow);
      String response = HelpClass.bookATable(userEmail, name, date_time, personCount, "2");

      if (response == null) {

        return handlerInput.getResponseBuilder()
            .withSpeech(
                "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
            .build();

      }

      Gson gson = new Gson();
      Booking responseBooking = gson.fromJson(response, Booking.class);

      HelpClass.req = new RequestOrder();
      // Def. Adr
      HelpClass.req.order.city = "Bad Belzig";
      HelpClass.req.order.street = "Am Kurpark";
      HelpClass.req.order.streetNr = "1A";

      HelpClass.req.booking.bookingToken = responseBooking.bookingToken;

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
