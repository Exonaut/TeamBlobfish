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

public class StartedDeliveryService implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("yesNoCustomerDetails").getValue() != null
        && intentRequest.getIntent().getSlots().get("dishOrder").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot yesNoCustomerDetails = intentRequest.getIntent().getSlots().get("yesNoCustomerDetails");

    if (yesNoCustomerDetails.getValue().equals("ja"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr erstes Gericht?").withReprompt("Was möchten Sie essen?").build();

    if (yesNoCustomerDetails.getValue().equals("nein"))
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoCustomerDetails", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Wollen Sie mit diesen Daten Name: "
            + HelpClass.req.booking.name + "\n" + " Anzahl der Gäste: " + HelpClass.req.booking.assistants
            + "\n" + "Buchungsemail: " + HelpClass.req.booking.email + "\n" + " fortfahren?")
        .withReprompt("Wollen Sie mit diese Daten fortfahren?").build();
  }

}
