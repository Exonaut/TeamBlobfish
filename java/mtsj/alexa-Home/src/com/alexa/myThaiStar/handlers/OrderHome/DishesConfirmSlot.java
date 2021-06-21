package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class DishesConfirmSlot implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("dishOrder").getValue() != null
        && intentRequest.getIntent().getSlots().get("extra").getValue() != null
        && intentRequest.getIntent().getSlots().get("amount").getValue() != null
        && intentRequest.getIntent().getSlots().get("amount").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot dish = intentRequest.getIntent().getSlots().get("dishOrder");
    Slot extra = intentRequest.getIntent().getSlots().get("extra");
    Slot amount = intentRequest.getIntent().getSlots().get("amount");

    boolean isNumeric = amount.getValue().chars().allMatch(Character::isDigit);
    if (!isNumeric)
      return handlerInput.getResponseBuilder().addElicitSlotDirective("amount", intentRequest.getIntent()).withSpeech(
          "Es tut mir leid, ich habe Sie leider nicht verstanden. Wie oft möchten Sie, dass von Ihnen ausgewählte Gericht bestellen ?")
          .withReprompt("Wie oft ?").build();

    String speechText = "Ich habe zu Ihrer Bestellung " + dish.getValue() + " mit " + extra.getValue() + " "
        + amount.getValue() + " mal hinzugefügt. Ist dies korrekt?";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("amount", intentRequest.getIntent())
        .withSpeech(speechText).withReprompt(speechText).build();
  }

}
