package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

public class AnotherDishYesNo implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && !intentRequest.getIntent().getSlots().get("amount").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("yesNoOne").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("yesNoOne", intentRequest.getIntent())
        .withSpeech("Möchten Sie noch etwas zum essen bestellen?").withReprompt("Darf es noch etwa sein?").build();

  }

}
