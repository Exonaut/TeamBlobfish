package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelpClass;

public class WhichExtrasDishes implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("dishOrder").getValue() != null
        && intentRequest.getIntent().getSlots().get("extra").getValue() == null
        && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot dish = intentRequest.getIntent().getSlots().get("dishOrder");

    HelpClass.dishID = HelpClass.getDishId(dish.getValue());

    if (HelpClass.dishID == null)
      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech("Ich habe Sie leider nicht verstanden. Welches Gericht möchten Sie?")
          .withReprompt("Welches Gericht möchten Sie?").build();

    String speechText = HelpClass.getExtrasName(HelpClass.dishID);

    if (speechText == null)
      return handlerInput.getResponseBuilder()
          .withSpeech("Es tut uns leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
          .build();

    return handlerInput.getResponseBuilder().addElicitSlotDirective("extra", intentRequest.getIntent())
        .withSpeech(speechText + " Wenn Sie keine Extras möchten, dann sagen Sie: ohne extras.")
        .withReprompt("Welche Extras möchten Sie?").build();

  }

}
