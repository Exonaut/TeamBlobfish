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

/**
 *
 * Check the serving time or enter it again
 *
 */
public class CloseOrderOrCorrectTheServeTime implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() != null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot serveTime = intentRequest.getIntent().getSlots().get("servingTime");
    String bookingDateTime = BasicOperations.convertMillisecondsToDateTime(BasicOperations.bookingDateTimeMilliseconds);
    String bookingTime = BasicOperations.getTimeFormat(bookingDateTime);
    String bookingDate = BasicOperations.getDateFormat(bookingDateTime);

    if (!BasicOperations.compareBookingTimeServeTime(bookingTime, serveTime.getValue())) { // serving time must be after
                                                                                           // the booking time

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech("Die Servierzeit " + serveTime.getValue() + " Uhr liegt nicht hinter Ihrer Buchungszeit"
              + bookingTime + " Uhr. Geben Sie die Servierzeit erneut an. Welche Servierzeit wünschen Sie?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    BasicOperations.req.order.serveTime = BasicOperations
        .getFormatDateTimeAndSubtractTwoHours(bookingDate + " " + serveTime.getValue());

    return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

  }

}
