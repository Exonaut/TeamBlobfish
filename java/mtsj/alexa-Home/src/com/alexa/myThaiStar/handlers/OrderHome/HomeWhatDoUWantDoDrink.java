package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

public class HomeWhatDoUWantDoDrink implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && intentRequest.getIntent().getSlots().get("yesNoDrink").getValue() != null
        && intentRequest.getIntent().getSlots().get("drink").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    if (intentRequest.getIntent().getSlots().get("yesNoDrink").getValue().equals("ja")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Welches Getränk möchten Sie?").withReprompt("Welches Getränk möchten Sie?").build();

    } else if (intentRequest.getIntent().getSlots().get("yesNoDrink").getValue().equals("nein")) {

      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoDrink", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Möchten Sie etwas zum trinken bestellen?")
        .withReprompt("Möchten Sie etwas trinken?").build();

  }

}
