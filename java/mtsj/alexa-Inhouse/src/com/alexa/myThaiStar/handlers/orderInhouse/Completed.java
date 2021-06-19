package com.alexa.myThaiStar.handlers.orderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.model.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelpClass;

public class Completed implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.COMPLETED);
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot queryTable = intentRequest.getIntent().getSlots().get("queryTable");
    Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (queryTable.getConfirmationStatusAsString().equals("DENIED"))
      return handlerInput.getResponseBuilder().withSpeech("Es tut mir leid, bitte starten Sie Ihre Bestellung erneut")
          .build();

    if (!sessionAttributes.containsValue(Attributes.START_STATE_RESERVATION))
      return handlerInput.getResponseBuilder()
          .withSpeech("Es tut mir leid, zu der jetzigen Uhrzeit wurde der Tisch nicht reserviert").build();

    String speechText = HelpClass.sendOrder();

    return handlerInput.getResponseBuilder().withSpeech(speechText).build();
  }

}
