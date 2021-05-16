package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

/**
 * TODO Spielecke This type ...
 *
 */
public class AmountGivenConfirmSlot implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menu").getValue() != null
        && intentRequest.getIntent().getSlots().get("extra").getValue() != null
        && intentRequest.getIntent().getSlots().get("amount").getValue() != null
        && intentRequest.getIntent().getSlots().get("amount").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menu = intentRequest.getIntent().getSlots().get("menu");
    Slot extra = intentRequest.getIntent().getSlots().get("extra");
    Slot amount = intentRequest.getIntent().getSlots().get("amount");

    String speechText = "Ich habe zu Ihrer Bestellung " + menu.getValue() + " mit " + extra.getValue() + " "
        + amount.getValue() + " mal hinzugefügt. Ist dies korrekt?";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("amount", intentRequest.getIntent())
        .withSpeech(speechText).withReprompt(speechText).build();
  }

}
