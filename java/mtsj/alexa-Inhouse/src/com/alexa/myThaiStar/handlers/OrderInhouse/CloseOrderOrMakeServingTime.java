package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.BasicOperations;

public class CloseOrderOrMakeServingTime implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("serveTimeYesNo").getValue() != null
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot serveTimeYesNo = intentRequest.getIntent().getSlots().get("serveTimeYesNo");

    if (serveTimeYesNo.getValue().equals("nein")) {

      String currentTime = BasicOperations
          .getTimeFormat(BasicOperations.convertMillisecondsToDateTime(System.currentTimeMillis() + 7200000));

      String currentDate = BasicOperations
          .getDateFormat(BasicOperations.convertMillisecondsToDateTime(System.currentTimeMillis() + 7200000));

      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

      Date serTime = null;
      try {
        serTime = sdf.parse(currentTime);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      Calendar calSerTime = Calendar.getInstance();
      calSerTime.setTime(serTime);
      calSerTime.add(Calendar.MINUTE, 30);

      SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

      BasicOperations.req.order.serveTime = BasicOperations
          .getFormatDateTimeAndCalculate(currentDate + " " + timeFormat.format(calSerTime.getTime()));

      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    } else if (serveTimeYesNo.getValue().equals("ja")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech("Welche Servierzeit wünschen Sie?").withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("serveTimeYesNo", intentRequest.getIntent())
        .withSpeech("Ich habe sie leider nicht verstanden. Möchten Sie sich das Essen liefern lassen ?")
        .withReprompt("Möchten Sie sich das Essen liefern lassen ?").build();

  }
}
