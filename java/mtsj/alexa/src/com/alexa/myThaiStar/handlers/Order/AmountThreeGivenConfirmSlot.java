package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class AmountThreeGivenConfirmSlot implements IntentRequestHandler {

  public static String BASE_URL;

  public AmountThreeGivenConfirmSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menuThree").getValue() != null
        && intentRequest.getIntent().getSlots().get("extraThree").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountThree").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountThree").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menuThree = intentRequest.getIntent().getSlots().get("menuThree");
    Slot extraThree = intentRequest.getIntent().getSlots().get("extraThree");
    Slot amountThree = intentRequest.getIntent().getSlots().get("amountThree");

    String speechText = "Ich habe zu Ihrer Bestellung " + menuThree.getValue() + " mit " + extraThree.getValue() + " "
        + amountThree.getValue() + " mal hinzugefügt. Ist dies korrekt?";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("amountThree", intentRequest.getIntent())
        .withSpeech(speechText).withReprompt(speechText).build();
  }

}
