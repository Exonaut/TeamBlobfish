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

public class AmountDrinks implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("drink").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountDrinks").getValue() == null;
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot drink = intentRequest.getIntent().getSlots().get("drink");

    HelpClass.dishID = HelpClass.getDishId(drink.getValue());

    if (HelpClass.dishID == null) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent()).withSpeech(
          "Es tut mir leid, dieses Getränk haben wir leider nicht auf der Getränkekarte. Bitte wählen Sie ein Getränk aus, welches auf der Getränkekarte vorhanden ist.")
          .withReprompt("Welches Getränk möchten Sie?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("amountDrinks", intentRequest.getIntent())
        .withSpeech("Wie oft möchten Sie, dass von Ihnen ausgewählte Getränk bestellen ?").withReprompt("Wie oft ?")
        .build();
  }

}
