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

public class CloseOrderOrMakeServingTime implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("deliveryServiceYesNo").getValue() != null
        && intentRequest.getIntent().getSlots().get("servingTime").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot deliveryServiceYesNo = intentRequest.getIntent().getSlots().get("deliveryServiceYesNo");
    String bookingDateTime = HelperOrderClass
        .convertMillisecondsToDateTime(HelperOrderClass.bookingDateTimeMilliseconds);
    String bookingTime = HelperOrderClass.getTimeFormat(bookingDateTime);
    String bookingDate = HelperOrderClass.getDateFormat(bookingDateTime);

    if (deliveryServiceYesNo.getValue().equals("ja")) {

      // TODO Lieferung yesNo in DB ?
      HelperOrderClass.req.order.serveTime = HelperOrderClass
          .getFormatDateTimeAndCalculate(bookingDate + " " + bookingTime);

      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent())
          .withSpeech("Vielen Dank für Ihre Bestellung. Wir werden Ihr Essen schnellstmöglich liefern lassen.").build();

    } else if (deliveryServiceYesNo.getValue().equals("nein")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("servingTime", intentRequest.getIntent())
          .withSpeech("Sie haben am " + bookingDate + " um " + bookingTime
              + " Uhr, einen Tisch reserviert. Sie können jetzt eine Servierzeit angeben. Welche Servierzeit wünschen Sie?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("deliveryServiceYesNo", intentRequest.getIntent())
        .withSpeech("Ich habe sie leider nicht verstanden. Möchten Sie sich das Essen liefern lassen ?")
        .withReprompt("Möchten Sie sich das Essen liefern lassen ?").build();

  }
}
