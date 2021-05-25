package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class AnotherDishOrDoUWantToDrink implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() != null
        && intentRequest.getIntent().getSlots().get("yesNoDrink").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot yesNo = intentRequest.getIntent().getSlots().get("yesNoEat");

    if (yesNo.getValue().equals("ja")) {

      Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amount").withValue(null).build();
      intentRequest.getIntent().getSlots().put("amount", updateSlot);
      Slot updateSlot2 = Slot.builder().withName("extra").withValue(null).build();
      intentRequest.getIntent().getSlots().put("extra", updateSlot2);
      Slot updateSlot3 = Slot.builder().withName("dishOrder").withValue(null).build();
      intentRequest.getIntent().getSlots().put("dishOrder", updateSlot3);
      Slot updateSlot4 = Slot.builder().withName("yesNoEat").withValue(null).build();
      intentRequest.getIntent().getSlots().put("yesNoEat", updateSlot4);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr weiteres Gericht?").withReprompt("Was möchten Sie essen?").build();
    } else if (yesNo.getValue().equals("nein")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoDrink", intentRequest.getIntent())
          .withSpeech("Möchten Sie etwas zum trinken bestellen?").withReprompt("Möchten Sie etwas trinken?").build();
    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoEat", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Möchten Sie noch etwas zum essen bestellen?")
        .withReprompt("Möchten Sie noch etwas essen?").build();

  }

}
