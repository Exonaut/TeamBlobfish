package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.BasicOperations;

/**
 *
 * Choose the number of the drink.
 *
 */

public class AmountDrinks implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && (intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("drink").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountDrinks").getValue() == null)
        || (intentRequest.getDialogState() == DialogState.STARTED
            && handlerInput.getAttributesManager().getSessionAttributes().containsKey("state")// if startet order
            // information already
            // saved. for example
            // checking the booking
            // id
            && intentRequest.getIntent().getSlots().get("drink").getValue() != null
            && intentRequest.getIntent().getSlots().get("amountDrinks").getValue() == null);
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot drink = intentRequest.getIntent().getSlots().get("drink");

    BasicOperations.dishID = BasicOperations.getDishId(drink.getValue());

    // drink does not exist
    if (BasicOperations.dishID == null)
      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent()).withSpeech(
          "Es tut mir leid, dieses Getränk haben wir leider nicht auf der Getränkekarte. Bitte wählen Sie ein Getränk aus, welches auf der Getränkekarte vorhanden ist.")
          .withReprompt("Welches Getränk möchten Sie?").build();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("amountDrinks", intentRequest.getIntent())
        .withSpeech("Wie oft möchten Sie, dass von Ihnen ausgewählte Getränk bestellen ?").withReprompt("Wie oft ?")
        .build();
  }

}
