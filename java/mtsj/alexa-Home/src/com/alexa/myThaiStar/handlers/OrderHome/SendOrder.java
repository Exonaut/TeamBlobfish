package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.attributes.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.tools.BasicOperations;

/**
 *
 * send order
 *
 */
public class SendOrder implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("sendOrder")));
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (attributes.containsValue(Attributes.START_STATE_WHERE_LIKE_TO_EAT_DELIVER)) {

      String speechText = BasicOperations.sendOrder();

      if (speechText == null)
        return handlerInput.getResponseBuilder()
            .withSpeech(
                "Es tut uns leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
            .build();

      return handlerInput.getResponseBuilder()
          .withSpeech(speechText + " Wir werden Ihre Bestellung schnellstmöglich liefern lassen.")
          .withShouldEndSession(true).build();
    }

    String speechText = BasicOperations.sendOrder();
    if (speechText == null)
      return handlerInput.getResponseBuilder()
          .withSpeech("Es tut uns leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
          .withShouldEndSession(true).build();

    return handlerInput.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true).build();
  }

}
