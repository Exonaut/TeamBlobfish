package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.model.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

public class QueryTable implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.STARTED);

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();
    sessionAttributes.put(Attributes.STATE_KEY, Attributes.START_STATE);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("queryTable", intentRequest.getIntent())
        .withSpeech("Wie lautet Ihre Tischnummer?")
        .withReprompt("Wie lautet Ihre Tischnummer? Ihre Tischnummer finden Sie in Ihrer Buchungsbestätigung.").build();
  }

}
