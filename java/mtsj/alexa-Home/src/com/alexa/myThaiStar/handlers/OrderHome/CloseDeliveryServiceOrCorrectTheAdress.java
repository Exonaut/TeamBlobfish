package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelpClass;

public class CloseDeliveryServiceOrCorrectTheAdress implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && !intentRequest.getIntent().getSlots().get("streetNr").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot street = intentRequest.getIntent().getSlots().get("street");
    Slot streetNr = intentRequest.getIntent().getSlots().get("streetNr");
    Slot city = intentRequest.getIntent().getSlots().get("city");

    if (streetNr.getConfirmationStatusAsString().equals("CONFIRMED")) {

      HelpClass.req.order.city = city.getValue();
      HelpClass.req.order.street = street.getValue();
      HelpClass.req.order.streetNr = streetNr.getValue();

      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    }

    Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("streetNr").withValue(null).build();
    intentRequest.getIntent().getSlots().put("streetNr", updateSlot);
    Slot updateSlot2 = Slot.builder().withName("city").withValue(null).build();
    intentRequest.getIntent().getSlots().put("city", updateSlot2);
    Slot updateSlot3 = Slot.builder().withName("street").withValue(null).build();
    intentRequest.getIntent().getSlots().put("street", updateSlot3);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("city", intentRequest.getIntent())
        .withSpeech("Bitte geben Sie Ihre Adresse erneut ein. Wie lautet die Stadt, in der Sie wohnen?")
        .withReprompt("Wie lautet die Stadt, in der Sie wohnen?").build();

  }

}
