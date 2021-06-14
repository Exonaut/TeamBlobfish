package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class StartEatOrDrink implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("eatOrDrink").getValue() != null
        && intentRequest.getIntent().getSlots().get("yesNoDrink").getValue() == null
        && intentRequest.getIntent().getSlots().get("dishOrder").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot eatOrDrink = intentRequest.getIntent().getSlots().get("eatOrDrink");

    if (eatOrDrink.getValue().equals("essen"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr erstes Gericht?").withReprompt("Was möchten Sie essen?").build();

    if (eatOrDrink.getValue().equals("trinken"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Was möchten Sie trinken?").withReprompt("Was möchten Sie zum trinken?").build();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("eatOrDrink", intentRequest.getIntent())
        .withSpeech("Möchten Sie mit Essen oder Trinken beginnnen?")
        .withReprompt("Möchten Sie mit Essen oder Trinken beginnnen?").build();
  }

}
