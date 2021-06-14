package com.alexa.myThaiStar.handlers.OrderInhouse;

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

    return (handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() != null
        && intentRequest.getIntent().getSlots().get("yesNoDrink").getValue() == null
        && intentRequest.getIntent().getSlots().get("serveTimeYesNo").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot yesNoEat = intentRequest.getIntent().getSlots().get("yesNoEat");
    Slot drink = intentRequest.getIntent().getSlots().get("drink");

    if (yesNoEat.getValue().equals("ja")) {

      Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amount").withValue(null).build();
      intentRequest.getIntent().getSlots().put("amount", updateSlot);
      Slot updateSlot2 = Slot.builder().withName("extra").withValue(null).build();
      intentRequest.getIntent().getSlots().put("extra", updateSlot2);
      Slot updateSlot3 = Slot.builder().withName("dishOrder").withValue(null).build();
      intentRequest.getIntent().getSlots().put("dishOrder", updateSlot3);
      Slot updateSlot4 = Slot.builder().withName("yesNoEat").withValue(null).build();
      intentRequest.getIntent().getSlots().put("yesNoEat", updateSlot4);

      if (drink.getValue() != null)
        return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
            .withSpeech("Wie lautet Ihr Gericht?").withReprompt("Was möchten Sie essen?").build();

      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr weiteres Gericht?").withReprompt("Was möchten Sie essen?").build();
    } else if (yesNoEat.getValue().equals("nein") && drink.getValue() != null) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("serveTimeYesNo", intentRequest.getIntent())
          .withSpeech(
              "Sie haben die Möglichkeit, eine Servierzeit anzugeben. Wenn Sie keine Servierzeit angeben möchten, "
                  + "wird Ihnen Ihr Essen in 30 minuten serviert. Wenn Sie eine Servierzeit angeben möchten, "
                  + "dann muss die Servierzeit mindestens 30 minuten hinter der aktuellen Zeit liegen. "
                  + "Möchten Sie eine Servierzeit angeben?")
          .withReprompt("Welche Servierzeit wünschen Sie?").build();

    }

    else if (yesNoEat.getValue().equals("nein")) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoDrink", intentRequest.getIntent())
          .withSpeech("Möchten Sie etwas zum trinken bestellen?").withReprompt("Möchten Sie etwas trinken?").build();
    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoEat", intentRequest.getIntent())
        .withSpeech("Ich habe Sie leider nicht verstanden. Möchten Sie noch etwas zum essen bestellen?")
        .withReprompt("Möchten Sie noch etwas essen?").build();

  }

}
