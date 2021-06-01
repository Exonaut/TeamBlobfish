package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelperOrderClass;

public class CloseOrder implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() != null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot serveTime = intentRequest.getIntent().getSlots().get("servingTime");
    String bookingDateTime = HelperOrderClass.convertSecondsToDateTime(HelperOrderClass.req.booking.bookingDate);
    String bookingTime = HelperOrderClass.getTimeFormat(bookingDateTime, 0);
    String bookingDate = HelperOrderClass.getDateFormat(bookingDateTime);

    if (!HelperOrderClass.compareTime(bookingTime, serveTime.getValue())) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech("Die Servierzeit, " + serveTime.getValue()
              + " muss hinter Ihrer Buchungszeit liegen. Welche Servierzeit wünschen Sie? ")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    HelperOrderClass.req.order.serveTime = HelperOrderClass
        .getFormatDateTimeAndCalculate(bookingDate + " " + serveTime.getValue());

    return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

  }

}
