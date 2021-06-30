package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.orderline.OrderLines;
import com.tools.BasicOperations;

public class AnotherDrinkYesNoOrCorrectTheDrink implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && !intentRequest.getIntent().getSlots().get("amountDrinks").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("yesNoAnotherDrink").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot confirmDrinks = intentRequest.getIntent().getSlots().get("amountDrinks");

    if (confirmDrinks.getConfirmationStatusAsString().equals("CONFIRMED")) {

      Slot amountDrinks = intentRequest.getIntent().getSlots().get("amountDrinks");

      OrderLines tmpOrderline = new OrderLines();

      tmpOrderline.orderLine.amount = amountDrinks.getValue();
      tmpOrderline.orderLine.dishId = BasicOperations.dishID;

      BasicOperations.req.orderLines.add(tmpOrderline);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoAnotherDrink", intentRequest.getIntent())
          .withSpeech("Möchten Sie noch etwas zum trinken bestellen?")
          .withReprompt("Darf es noch etwas zum trinken sein?").build();

    }

    Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amountDrinks").withValue(null).build();
    intentRequest.getIntent().getSlots().put("amountDrinks", updateSlot);
    Slot updateSlot2 = Slot.builder().withName("drink").withValue(null).build();
    intentRequest.getIntent().getSlots().put("drink", updateSlot2);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
        .withSpeech("Bitte geben Sie Ihr Getränk erneut ein. Was möchten Sie zum trinken? ")
        .withReprompt("Was möchten Sie noch zum trinken?").build();

  }

}
