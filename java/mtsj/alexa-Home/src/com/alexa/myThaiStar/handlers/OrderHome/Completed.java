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

public class Completed implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.COMPLETED);
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    // Slot counterBookingIDs = intentRequest.getIntent().getSlots().get("counterBookingIDs");

    Slot yesNoCustomerDetails = intentRequest.getIntent().getSlots().get("yesNoCustomerDetails");

    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");

    if (HelperOrderClass.counterBookingIDs == 0)
      return handlerInput.getResponseBuilder()
          .withSpeech(
              "Keine Buchungs ID gefunden. Bitte buchen Sie zuerst einen Tisch, bevor Sie eine Bestellung vornehmen.")
          .build();

    if (yesNoCustomerDetails != null) {

      if (yesNoCustomerDetails.getValue().equals("nein")) {

        return handlerInput.getResponseBuilder()
            .withSpeech("Es tut mir leid. Es gibt keine Auswahlmöglichkeit für eine spätere Reservierung").build();

      }

    }

    if (whereLikeToEat.getValue().equals("liefern")) {

      HelperOrderClass.req.order.deliveryservice = "" + 1;

      return handlerInput.getResponseBuilder()
          .withSpeech(HelperOrderClass.sendOrder() + " Wir werden Ihre Bestellung schnellstmöglich liefern lassen")
          .build();
    }
    return handlerInput.getResponseBuilder().withSpeech(HelperOrderClass.sendOrder()).build();
  }

}
