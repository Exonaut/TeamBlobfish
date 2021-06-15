package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.entity.booking.Booking;
import com.entity.booking.ResponseBooking;
import com.tools.HelpClass;

public class CustomerDetailsConfirmSlot implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("queryTable").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("queryTable").getValue() != null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    ResponseBooking response = HelpClass.getAllBookings();

    Booking booking = HelpClass.tableBooked(response, intentRequest);

    if (booking == null)
      return handlerInput.getResponseBuilder()
          .withSpeech("Es ist ein Problem aufgetreten. Bitte versuchen Sie es zu einem späteren Zeitpunkt.").build();

    String assistant = " Gästen";
    if (Integer.parseInt(HelpClass.req.booking.assistants) == 1)
      assistant = " Gast";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("queryTable", intentRequest.getIntent())
        .withSpeech("Dieser Tisch ist reserviert für " + HelpClass.req.booking.name + "\n" + "mit "
            + HelpClass.req.booking.assistants + assistant + ". Sind Ihre Daten korrekt?")
        .withReprompt("Sind Ihre Daten korrekt?").build();
  }
}
