package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class HomeDrinksConfirmSlot implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("amountDrinks").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountDrinks").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot drink = intentRequest.getIntent().getSlots().get("drink");
    Slot amountDrinks = intentRequest.getIntent().getSlots().get("amountDrinks");

    String speechText = "Ich habe zu Ihrer Bestellung " + drink.getValue() + " " + amountDrinks.getValue()
        + " mal hinzugefügt. Ist dies korrekt?";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("amountDrinks", intentRequest.getIntent())
        .withSpeech(speechText).withReprompt(speechText).build();
  }

}
