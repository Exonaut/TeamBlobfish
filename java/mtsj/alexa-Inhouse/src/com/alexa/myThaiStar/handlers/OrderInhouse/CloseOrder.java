package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.BasicOperations;

public class CloseOrder implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() != null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot serveTime = intentRequest.getIntent().getSlots().get("servingTime");

    String currentTime = BasicOperations
        .getTimeFormat(BasicOperations.convertMillisecondsToDateTime(System.currentTimeMillis() + 7200000));

    String currentDate = BasicOperations
        .getDateFormat(BasicOperations.convertMillisecondsToDateTime(System.currentTimeMillis() + 7200000));

    if (!BasicOperations.compareCurrentTimeServeTime(serveTime.getValue(), currentTime)) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech(
              "Die Servierzeit, " + serveTime.getValue() + " Uhr liegt nicht 30 minuten hinter der aktuellen Zeit "
                  + currentTime + " Uhr. Geben Sie die Servierzeit erneut an. Welche Servierzeit wünschen Sie?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    BasicOperations.req.order.serveTime = BasicOperations
        .getFormatDateTimeAndCalculate(currentDate + " " + serveTime.getValue());

    return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

  }

}
