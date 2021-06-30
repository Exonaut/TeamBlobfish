package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

/**
 *
 * Every order must start here. Here you are asked where the user would like to eat
 *
 */
public class StartetOrderInformationWhereLikeToEat implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.STARTED);

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    if (handlerInput.getAttributesManager().getSessionAttributes().containsKey("state")) // order was already startet
      return handlerInput.getResponseBuilder().withSpeech("Ich habe Sie leider nicht verstanden. Was möchten Sie?")
          .withReprompt("Was möchten Sie ?").build();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("whereLikeToEat", intentRequest.getIntent())
        .withSpeech(
            "Wollen Sie sich die Bestellung liefern lassen oder ist die Bestellung für einen Tisch im Restaurant?")
        .withReprompt("Wo möchten Sie essen?").build();
  }

}
