package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.model.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.orderline.Extras;
import com.entity.orderline.OrderLines;
import com.tools.BasicOperations;

public class AnotherDishYesNoOrCorrectTheDish implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && !intentRequest.getIntent().getSlots().get("amount").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot confirmedDish = intentRequest.getIntent().getSlots().get("amount");
    Slot extra = intentRequest.getIntent().getSlots().get("extra");
    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    if (confirmedDish.getConfirmationStatusAsString().equals("CONFIRMED")) {

      OrderLines tmpOrderline = new OrderLines();

      ArrayList<Extras> extrasArray = new ArrayList<>();

      for (Extras s : BasicOperations.extras) {
        if (extra.getValue().contains(s.name.toLowerCase())) {
          Extras extras = new Extras();
          extras.id = s.id;
          extrasArray.add(extras);
        }
      }

      tmpOrderline.extras.addAll(extrasArray);
      tmpOrderline.orderLine.amount = intentRequest.getIntent().getSlots().get("amount").getValue();
      tmpOrderline.orderLine.dishId = BasicOperations.dishID;

      BasicOperations.req.orderLines.add(tmpOrderline);

      if (attributes.containsKey(Attributes.STATE_KEY_ONLY_ADD_INDIVIDUAL))
        return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoEat", intentRequest.getIntent())
          .withSpeech("Möchten Sie noch etwas zum essen bestellen?").withReprompt("Darf es noch etwas sein?").build();

    }

    Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amount").withValue(null).build();
    intentRequest.getIntent().getSlots().put("amount", updateSlot);
    Slot updateSlot2 = Slot.builder().withName("extra").withValue(null).build();
    intentRequest.getIntent().getSlots().put("extra", updateSlot2);
    Slot updateSlot3 = Slot.builder().withName("dishOrder").withValue(null).build();
    intentRequest.getIntent().getSlots().put("dishOrder", updateSlot3);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
        .withSpeech("Bitte geben Sie das Gericht erneut ein. Was möchten Sie essen?")
        .withReprompt("Was möchten Sie essen?").build();

  }

}
