package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class AdressConfirmSlot implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("city").getValue() != null
        && intentRequest.getIntent().getSlots().get("street").getValue() != null
        && intentRequest.getIntent().getSlots().get("streetNr").getValue() != null
        && intentRequest.getIntent().getSlots().get("streetNr").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot city = intentRequest.getIntent().getSlots().get("city");
    Slot street = intentRequest.getIntent().getSlots().get("street");
    Slot streetNr = intentRequest.getIntent().getSlots().get("streetNr");

    String name = handlerInput.getServiceClientFactory().getUpsService().getProfileName();

    String speechText = "Ihre Bestellung wird geliefert an:" + name + "\n" + "Stadt: " + city.getValue() + "\n"
        + "Straße:" + street.getValue() + " " + streetNr.getValue() + " \n Ist dies korrekt?";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("streetNr", intentRequest.getIntent())
        .withSpeech(speechText).withReprompt(speechText).build();
  }

}
