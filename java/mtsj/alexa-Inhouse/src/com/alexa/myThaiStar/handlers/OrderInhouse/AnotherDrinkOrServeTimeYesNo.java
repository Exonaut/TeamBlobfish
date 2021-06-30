package com.alexa.myThaiStar.handlers.OrderInhouse;

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

public class AnotherDrinkOrServeTimeYesNo implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("yesNoAnotherDrink").getValue() != null
        && !intentRequest.getIntent().getSlots().get("yesNoAnotherDrink").getValue().equals("nichts")
        && intentRequest.getIntent().getSlots().get("serveTimeYesNo").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot yesNoAnotherDrink = intentRequest.getIntent().getSlots().get("yesNoAnotherDrink");
    Slot dishOrder = intentRequest.getIntent().getSlots().get("dishOrder");
    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (yesNoAnotherDrink.getValue().equals("ja")) {

      Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amountDrinks").withValue(null).build();
      intentRequest.getIntent().getSlots().put("amountDrinks", updateSlot);
      Slot updateSlot3 = Slot.builder().withName("drink").withValue(null).build();
      intentRequest.getIntent().getSlots().put("drink", updateSlot3);
      Slot updateSlot5 = Slot.builder().withName("yesNoAnotherDrink").withValue(null).build();
      intentRequest.getIntent().getSlots().put("yesNoAnotherDrink", updateSlot5);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
          .withSpeech("Was möchten Sie noch zum trinken?").withReprompt("Was möchten Sie noch zum trinken?").build();
    }

    else if (yesNoAnotherDrink.getValue().equals("nein") && !attributes.containsValue(Attributes.START_STATE_ORDER_EAT)
        && dishOrder.getValue() == null) {

      Slot updateSlot = Slot.builder().withName("yesNoAnotherDrink").withValue("nichts").build();
      intentRequest.getIntent().getSlots().put("yesNoAnotherDrink", updateSlot);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoEat", intentRequest.getIntent())
          .withSpeech("Möchten Sie etwas zum essen bestellen?").withReprompt("Darf es noch etwas zum essen sein?")
          .build();

    }

    else if (yesNoAnotherDrink.getValue().equals("nein")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("serveTimeYesNo", intentRequest.getIntent())
          .withSpeech(
              "Sie haben die Möglichkeit, eine Servierzeit anzugeben. Wenn Sie keine Servierzeit angeben möchten, "
                  + "wird Ihnen Ihr Essen in 30 minuten serviert. Wenn Sie eine Servierzeit angeben möchten, "
                  + "dann muss die Servierzeit mindestens 30 minuten hinter der aktuellen Zeit liegen. "
                  + "Möchten Sie eine Servierzeit angeben?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoAnotherDrink", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Möchten Sie noch etwas zum trinken bestellen?")
        .withReprompt("Möchten Sie noch etwas trinken?").build();

  }

}
