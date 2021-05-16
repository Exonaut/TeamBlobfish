package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelperOrderClass;

public class ShowExtras implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menu").getValue() != null
        && intentRequest.getIntent().getSlots().get("extra").getValue() == null
        && !intentRequest.getIntent().getSlots().get("yesNoOne").getConfirmationStatusAsString().equals("DENIED");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menu = intentRequest.getIntent().getSlots().get("menu");

    String dishId = HelperOrderClass.getDishId(menu.getValue());

    return handlerInput.getResponseBuilder().addElicitSlotDirective("extra", intentRequest.getIntent())
        .withSpeech(HelperOrderClass.getExtrasName(dishId) + " Wenn Sie keine Extras möchten, dann sagen Sie: nein Danke.")
        .withReprompt("Welche Extras möchten Sie?").build();

  }

}
