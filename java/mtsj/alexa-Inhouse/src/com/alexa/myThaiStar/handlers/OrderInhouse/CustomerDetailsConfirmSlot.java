package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

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

    Slot queryTable = intentRequest.getIntent().getSlots().get("queryTable");

    ResponseBooking response = HelpClass.getAllBookings();

    if (response == null)
      return handlerInput.getResponseBuilder()
          .withSpeech("Es ist ein Problem aufgetreten. Bitte versuchen Sie es zu einem späteren Zeitpunkt.").build();

    Booking booking = HelpClass.tableBooked(response, queryTable.getValue());

    if (booking == null) {
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();
    }

    Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
    sessionAttributes.put(Attributes.STATE_KEY_RESERVATION, Attributes.START_STATE_RESERVATION);

    String assistant = " Gästen";
    if (Integer.parseInt(HelpClass.req.booking.assistants) == 1)
      assistant = " Gast";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("queryTable", intentRequest.getIntent())
        .withSpeech("Dieser Tisch ist reserviert für " + HelpClass.req.booking.name + "\n" + "mit "
            + HelpClass.req.booking.assistants + assistant + ". Sind Ihre Daten korrekt?")
        .withReprompt("Sind Ihre Daten korrekt?").build();
  }
}
