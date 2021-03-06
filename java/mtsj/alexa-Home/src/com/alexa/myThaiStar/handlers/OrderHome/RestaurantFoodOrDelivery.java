package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.attributes.Attributes;
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

/**
 *
 * If restaurant was selected then it will be checked if a table was booked. If deliver was selected, then it is asked
 * whether to start eating or drinking.
 *
 */

public class RestaurantFoodOrDelivery implements IntentRequestHandler {

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

      // Save that is is a meal in the restaurant
      attributes.put(Attributes.STATE_KEY_WHERE_LIKE_TO_EAT, Attributes.START_STATE_WHERE_LIKE_TO_EAT_RESTAURANT);
      ResponseBooking response = BasicOperations.getAllBookingsAndOrders();

      if (response == null)
        return handlerInput.getResponseBuilder()
            .withSpeech("Es ist ein Problem aufgetreten. Bitte versuchen Sie es zu einem sp??teren Zeitpunkt.").build();

      String userEmail = handlerInput.getServiceClientFactory().getUpsService().getProfileEmail();
      Attributes.START_STATE_COUNTER_BOOKING_IDS = BasicOperations.bookingIDAvailable(response, userEmail); // how many
                                                                                                            // bookings
                                                                                                            // are there

      attributes.put(Attributes.STATE_KEY_COUNTER_BOOKING_IDS, Attributes.START_STATE_COUNTER_BOOKING_IDS);

      // no booking id available
      if (Attributes.START_STATE_COUNTER_BOOKING_IDS == 0) {

        return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

      }

      if (Attributes.START_STATE_COUNTER_BOOKING_IDS == 1) {
        String bookingDateTime = BasicOperations
            .convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
        String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
        String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

        String assistant = " G??sten";
        if (Integer.parseInt(BasicOperations.req.booking.assistants) == 1)
          assistant = " Gast";

        // check if the booking data apply to the customer
        return handlerInput.getResponseBuilder()
            .addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
            .withSpeech("Sie haben am " + bookingDate + " um " + bookingTime + " Uhr mit "
                + BasicOperations.req.booking.assistants + assistant
                + ", einen Tisch reserviert. Wollen Sie mit diesen Daten fortfahren?")
            .withReprompt("Wollen Sie mit diese Daten fortfahren?").build();
      }

      if (Attributes.START_STATE_COUNTER_BOOKING_IDS > 1) {
        String bookingDateTime = BasicOperations
            .convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
        String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
        String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

        String assistant = " G??sten";
        if (Integer.parseInt(BasicOperations.req.booking.assistants) == 1)
          assistant = " Gast";

        // check if the booking data apply to the customer
        return handlerInput.getResponseBuilder()
            .addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
            .withSpeech("Ich habe mehrere Eintr??ge gefunden. Sie haben am " + bookingDate + " um " + bookingTime
                + " Uhr mit " + BasicOperations.req.booking.assistants + assistant
                + ", einen Tisch reserviert. Wollen Sie mit diesen Daten fortfahren?")
            .withReprompt("Wollen Sie mit diesen Daten fortfahren?").build();
      }

    }

    if (whereLikeToEat.getValue().equals("liefern"))

    {

      attributes = handlerInput.getAttributesManager().getSessionAttributes();
      attributes.put(Attributes.STATE_KEY_WHERE_LIKE_TO_EAT, Attributes.START_STATE_WHERE_LIKE_TO_EAT_DELIVER); // Save
                                                                                                                // that
                                                                                                                // it is
                                                                                                                // a
                                                                                                                // delivery

      String personCount = "";
      String name = handlerInput.getServiceClientFactory().getUpsService().getProfileName();
      String userEmail = handlerInput.getServiceClientFactory().getUpsService().getProfileEmail();

      Date date = new Date();
      long timeNow = date.getTime();
      String date_time = BasicOperations.convertMillisecondsToDateTime(timeNow + 600000);
      String response = BasicOperations.bookATable(userEmail, name, date_time, personCount, "2"); // make a fake booking
                                                                                                  // to be able to place
                                                                                                  // an order
      // Connecting Problem
      if (response == null) {
        return handlerInput.getResponseBuilder()
            .withSpeech(
                "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem sp??teren Zeitpunkt.")
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
          .withSpeech("M??chten Sie mit Essen oder Trinken beginnen?")
          .withReprompt("M??chten Sie mit Essen oder Trinken beginnen?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("whereLikeToEat", intentRequest.getIntent())
        .withSpeech(
            " Ich habe Sie leider nicht verstanden. Wollen Sie sich die Bestellung liefern lassen oder ist die Bestellung f??r einen reservierten Tisch im Restaurant? Sie k??nnen sagen im Restaurant oder liefern lassen")
        .withReprompt("Wo m??chten Sie essen?").build();
  }

}
