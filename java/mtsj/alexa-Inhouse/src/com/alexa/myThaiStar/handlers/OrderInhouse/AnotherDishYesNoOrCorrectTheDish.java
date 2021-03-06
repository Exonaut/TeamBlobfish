package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.Optional;

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

    return handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS
        && !intentRequest.getIntent().getSlots().get("amount").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot confirmedDish = intentRequest.getIntent().getSlots().get("amount");
    Slot extra = intentRequest.getIntent().getSlots().get("extra");

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

      return handlerInput.getResponseBuilder().addElicitSlotDirective("yesNoEat", intentRequest.getIntent())
          .withSpeech("M??chten Sie noch etwas zum essen bestellen?").withReprompt("Darf es noch etwas sein?").build();

    }

    Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amount").withValue(null).build();
    intentRequest.getIntent().getSlots().put("amount", updateSlot);
    Slot updateSlot2 = Slot.builder().withName("extra").withValue(null).build();
    intentRequest.getIntent().getSlots().put("extra", updateSlot2);
    Slot updateSlot3 = Slot.builder().withName("dishOrder").withValue(null).build();
    intentRequest.getIntent().getSlots().put("dishOrder", updateSlot3);

    return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
        .withSpeech("Bitte geben Sie das Gericht erneut ein. Was m??chten Sie essen?")
        .withReprompt("Was m??chten Sie essen?").build();

  }

}
