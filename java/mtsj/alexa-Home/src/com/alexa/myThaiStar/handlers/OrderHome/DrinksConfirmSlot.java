package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class DrinksConfirmSlot implements IntentRequestHandler {
  /**
   * Verify that the drink has been properly recorded
   */
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

    boolean isNumeric = amountDrinks.getValue().chars().allMatch(Character::isDigit);

    if (!isNumeric)
      return handlerInput.getResponseBuilder().addElicitSlotDirective("amountDrinks", intentRequest.getIntent())
          .withSpeech(
              "Es tut mir leid, ich habe Sie leider nicht verstanden. Wie oft möchten Sie, dass von Ihnen ausgewählte Getränk bestellen ?")
          .withReprompt("Wie oft ?").build();

    String speechText = "Ich habe zu Ihrer Bestellung " + drink.getValue() + " " + amountDrinks.getValue()
        + " mal hinzugefügt. Ist dies korrekt?";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("amountDrinks", intentRequest.getIntent())
        .withSpeech(speechText).withReprompt(speechText).build();
  }

}
