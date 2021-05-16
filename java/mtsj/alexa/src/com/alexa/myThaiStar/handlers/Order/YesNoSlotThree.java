package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

public class YesNoSlotThree implements IntentRequestHandler {

  public static String BASE_URL;

  public YesNoSlotThree(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && !intentRequest.getIntent().getSlots().get("amountThree").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("yesNoThree").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("menuFour").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("yesNoThree", intentRequest.getIntent())
        .withSpeech("Möchten Sie noch etwas zum essen bestellen?").withReprompt("Darf es noch etwa sein?").build();

  }

}
