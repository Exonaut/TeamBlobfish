package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.attributes.Attributes;
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
    Map<String, Object> sessionAttributes = handlerInput.getAttributesManager().getSessionAttributes();

    sessionAttributes.put(Attributes.STATE_KEY, Attributes.START_STATE);

    if (eatOrDrink.getValue().equals("essen")) {

      sessionAttributes.put(Attributes.STATE_KEY_MENU, Attributes.START_STATE_MENU_EAT);
      sessionAttributes.put(Attributes.STATE_KEY_ORDER, Attributes.START_STATE_ORDER_EAT);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr erstes Gericht? ")
          .withReprompt(
              "Wenn Sie sich noch nicht sicher sind, was sie zum essen wollen, dann verlangen Sie einfach nach der Speisekarte.")
          .withShouldEndSession(false).build();
    }

    if (eatOrDrink.getValue().equals("trinken")) {

      sessionAttributes.put(Attributes.STATE_KEY_MENU, Attributes.START_STATE_MENU_DRINK);
      sessionAttributes.put(Attributes.STATE_KEY_ORDER, Attributes.START_STATE_ORDER_DRINK);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr erstes Getränk?")
          .withReprompt(
              "Wenn Sie sich noch nicht sicher sind, was sie zum trinken wollen, dann verlangen Sie einfach nach der Getränkekarte.")
          .withShouldEndSession(false).build();
    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("eatOrDrink", intentRequest.getIntent())
        .withSpeech("Möchten Sie mit Essen oder Trinken beginnnen?")
        .withReprompt("Möchten Sie mit Essen oder Trinken beginnnen?").build();
  }

}
