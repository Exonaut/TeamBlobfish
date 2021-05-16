package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

/**
 * TODO Spielecke This type ...
 *
 */
public class AmountOneGivenConfirmSlot implements IntentRequestHandler {

  public static String BASE_URL;

  public AmountOneGivenConfirmSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("menuOne").getValue() != null
        && intentRequest.getIntent().getSlots().get("extraOne").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountOne").getValue() != null
        && intentRequest.getIntent().getSlots().get("amountOne").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menuOne = intentRequest.getIntent().getSlots().get("menuOne");
    Slot extraOne = intentRequest.getIntent().getSlots().get("extraOne");
    Slot amountOne = intentRequest.getIntent().getSlots().get("amountOne");

    String speechText = "Ich habe zu Ihrer Bestellung " + menuOne.getValue() + " mit " + extraOne.getValue() + " "
        + amountOne.getValue() + " mal hinzugefügt. Ist dies korrekt?";

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("amountOne", intentRequest.getIntent())
        .withSpeech(speechText).withReprompt(speechText).build();
  }

}
