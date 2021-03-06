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
import com.entity.orderline.OrderLines;
import com.tools.BasicOperations;

/**
 *
 * If the drink is confirmed, then it is saved. Otherwise it is entered again
 *
 */
public class AnotherDrinkYesNoOrCorrectTheDrink implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && !intentRequest.getIntent().getSlots().get("amountDrinks").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("yesNoAnotherDrink").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot confirmDrinks = intentRequest.getIntent().getSlots().get("amountDrinks");
    Slot drink = intentRequest.getIntent().getSlots().get("drink");
    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (confirmDrinks.getConfirmationStatusAsString().equals("CONFIRMED")) {

      Slot amountDrinks = intentRequest.getIntent().getSlots().get("amountDrinks");

      BasicOperations.previousOrder.add(drink.getValue() + " " + confirmDrinks.getValue() + " mal");

      OrderLines tmpOrderline = new OrderLines();

      tmpOrderline.orderLine.amount = amountDrinks.getValue();
      tmpOrderline.orderLine.dishId = BasicOperations.dishID;

      BasicOperations.req.orderLines.add(tmpOrderline);

      if (attributes.containsKey(Attributes.STATE_KEY_ONLY_ADD_INDIVIDUAL)) // If a single dish must be added afterwards
        return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoAnotherDrink", intentRequest.getIntent())
          .withSpeech("M??chten Sie noch etwas zum trinken bestellen?")
          .withReprompt("Darf es noch etwas zum trinken sein?").build();

    }

    Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amountDrinks").withValue(null).build();
    intentRequest.getIntent().getSlots().put("amountDrinks", updateSlot);
    Slot updateSlot2 = Slot.builder().withName("drink").withValue(null).build();
    intentRequest.getIntent().getSlots().put("drink", updateSlot2);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("drink", intentRequest.getIntent())
        .withSpeech("Bitte geben Sie Ihr Getr??nk erneut ein. Was m??chten Sie zum trinken? ")
        .withReprompt("Was m??chten Sie noch zum trinken?").build();

  }

}
