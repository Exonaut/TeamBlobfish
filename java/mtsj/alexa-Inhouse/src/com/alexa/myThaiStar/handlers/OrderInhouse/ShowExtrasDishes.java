package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelperOrderClass;

public class ShowExtrasDishes implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("dishOrder").getValue() != null
        && intentRequest.getIntent().getSlots().get("extra").getValue() == null
        && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot dish = intentRequest.getIntent().getSlots().get("dishOrder");

    String dishId = HelperOrderClass.getDishId(dish.getValue());

    if (dishId == null)
      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Ich habe Sie leider nicht verstanden. Welches Gericht möchten Sie?")
          .withReprompt("Welches Gericht möchten Sie?").build();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("extra", intentRequest.getIntent())
        .withSpeech(
            HelperOrderClass.getExtrasName(dishId) + " Wenn Sie keine Extras möchten, dann sagen Sie: ohne extras.")
        .withReprompt("Welche Extras möchten Sie?").build();

  }

}
