package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.model.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

public class WhereLikeToEat implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome")) && intentRequest.getDialogState() == DialogState.STARTED)
        && intentRequest.getIntent().getSlots().get("dishOrder").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
    sessionAttributes.put(Attributes.STATE_KEY, Attributes.START_STATE);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("whereLikeToEat", intentRequest.getIntent())
        .withSpeech(
            "Wollen Sie sich die Bestellung liefern lassen oder ist die Bestellung für einen Tisch im Restaurant?")
        .withReprompt("Wo möchten Sie essen?").withShouldEndSession(false).build();
  }

}
