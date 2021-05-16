package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class AmountTwoGivenConfirmSlot implements IntentRequestHandler {

  public static String BASE_URL;

  public AmountTwoGivenConfirmSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menuTwo").getValue() != null
        && intentRequest.getIntent().getSlots().get("extraTwo").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountTwo").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountTwo").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menuTwo = intentRequest.getIntent().getSlots().get("menuTwo");
    Slot extraTwo = intentRequest.getIntent().getSlots().get("extraTwo");
    Slot amountTwo = intentRequest.getIntent().getSlots().get("amountTwo");

    String speechText = "Ich habe zu Ihrer Bestellung " + menuTwo.getValue() + " mit " + extraTwo.getValue() + " "
        + amountTwo.getValue() + " mal hinzugefügt. Ist dies korrekt?";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("amountTwo", intentRequest.getIntent())
        .withSpeech(speechText).withReprompt(speechText).build();
  }

}
