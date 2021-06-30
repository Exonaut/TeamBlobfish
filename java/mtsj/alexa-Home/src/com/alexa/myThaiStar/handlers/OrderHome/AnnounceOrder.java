
package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.tools.BasicOperations;

public class AnnounceOrder implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {

    return input.matches(intentName("announceOrder"))
        || input.getAttributesManager().getSessionAttributes().containsKey("state")
            && input.matches(intentName("announceOrder"));
  }

  @Override
  public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {

    String speechText = " Sie haben bisher noch keine Bestellung aufgenommen. Wenn Sie etwas bestellen möchten, dann sagen Sie: Ich möchte etwas bestellen.";

    if (BasicOperations.previousOrder == null || BasicOperations.previousOrder.isEmpty())
      return input.getResponseBuilder().withSpeech(speechText).build();

    speechText = " Was Sie bisher bestellt haben: ";
    for (String s : BasicOperations.previousOrder) {
      speechText += s + " ,";
    }

    return input.getResponseBuilder().withSpeech(speechText
        + "Sie können Ihrer Bestellung weitere Gerichte oder Getränke hinzufügen. Sagen Sie zum Beispiel: Ich nehme noch Thai green chicken curry. Sie können auch einfach nach der Speisekarte verlangen.")
        .withReprompt(
            "Sie können Ihrer Bestellung weitere Gerichte oder Getränke hinzufügen. Sagen Sie zum Beispiel: Ich nehme noch Thai green chicken curry. Sie können auch einfach nach der Speisekarte verlangen.")
        .build();
  }
}
