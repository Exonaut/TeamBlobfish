package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.booking.ResponseBooking;
import com.tools.HelperOrderClass;

public class DeliveryServiceOrCheckCustomerDetails implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("whereLikeToEat").getValue() != null
        && intentRequest.getIntent().getSlots().get("yesNoCustomerDetails").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");

    if (whereLikeToEat.getValue().equals("restaurant")) {

      ResponseBooking response = HelperOrderClass.getAllBookings();

      if (response == null)
        return handlerInput.getResponseBuilder()
            .withSpeech("Es ist ein Problem aufgetreten. Bitte versuchen Sie es zu einem späteren Zeitpunkt.").build();

      HelperOrderClass.counterBookingIDs = HelperOrderClass.bookingIDAvailable(response, handlerInput);

      if (HelperOrderClass.counterBookingIDs == 0) {
        // Intent.builder().withName("makeAOrderHome").putSlotsItem("counterBookingIDs",
        // Slot.builder().withName("AMAZON.FOUR_DIGIT_NUMBER").withValue("0").build()).build();

        return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();
      }

      if (HelperOrderClass.counterBookingIDs == 1) {

        return handlerInput.getResponseBuilder()
            .addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
            .withSpeech("Name: " + HelperOrderClass.req.booking.name + "\n" + " Anzahl der Gäste: "
                + HelperOrderClass.req.booking.assistants + "\n" + "Buchungsemail: "
                + HelperOrderClass.req.booking.email + "\n" + ". Sind Ihre Daten korrekt?")
            .withReprompt("Sind Ihre Daten korrekt?").build();

      }

      if (HelperOrderClass.counterBookingIDs > 1) {

        return handlerInput.getResponseBuilder()
            .addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
            .withSpeech(
                "Ich habe mehrere Einträge gefunden. Ihre Bestellung wird zu Name: " + HelperOrderClass.req.booking.name
                    + "\n" + " Anzahl der Gäste: " + HelperOrderClass.req.booking.assistants + "\n" + "Buchungsemail: "
                    + HelperOrderClass.req.booking.email + "\n" + ". zugeordnet. Ist dies korrekt?")
            .withReprompt("Ist dies korrekt?").build();

      }

    }

    if (whereLikeToEat.getValue().equals("liefern")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr erstes Gericht?").withReprompt("Was möchten Sie essen?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("whereLikeToEat", intentRequest.getIntent())
        .withSpeech("Sie sagten" + whereLikeToEat.getValue()
            + "Ich habe Sie leider nicht verstanden. Möchten Sie sich das Essen liefern lassen oder möchten Sie im "
            + "Restaurant essen? Sie können sagen im Restaurant oder liefern lassen")
        .withReprompt("Wo möchten Sie essen?").build();
  }

}
