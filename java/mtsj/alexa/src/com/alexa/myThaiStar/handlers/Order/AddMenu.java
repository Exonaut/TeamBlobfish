package com.alexa.myThaiStar.handlers.Order;

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
import com.tools.HelperOrderClass;

public class AddMenu implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && !intentRequest.getIntent().getSlots().get("yesNoOne").getConfirmationStatusAsString().equals("NONE");

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menu = intentRequest.getIntent().getSlots().get("menu");
    Slot extra = intentRequest.getIntent().getSlots().get("extra");

    OrderLines tmp = new OrderLines();
    ArrayList<Extras> extrasArray = new ArrayList<>();

    ArrayList<String> extrasNameArray = HelperOrderClass.getExtrasNameArray();

    for (String s : extrasNameArray) {

      if (extra.getValue().contains(s.toLowerCase())) {
        Extras extras = new Extras();
        extras.id = HelperOrderClass.getExtrasID(s.toLowerCase());
        extrasArray.add(extras);
      }

    }

    tmp.extras.addAll(extrasArray);

    tmp.orderLine.amount = intentRequest.getIntent().getSlots().get("amount").getValue();
    tmp.orderLine.dishId = HelperOrderClass.getDishId(menu.getValue());
    tmp.orderLine.comment = "";

    HelperOrderClass.req.orderLines.add(tmp);

    Slot yesNo = intentRequest.getIntent().getSlots().get("yesNoOne");

    if (yesNo.getConfirmationStatusAsString().equals("CONFIRMED")) {
      Slot updateSlot = Slot.builder().withConfirmationStatus("NONE").withName("amount").withValue(null).build();
      intentRequest.getIntent().getSlots().put("amount", updateSlot);
      Slot updateSlot2 = Slot.builder().withName("extra").withValue(null).build();
      intentRequest.getIntent().getSlots().put("extra", updateSlot2);
      Slot updateSlot3 = Slot.builder().withName("menu").withValue(null).build();
      intentRequest.getIntent().getSlots().put("menu", updateSlot3);

      return handlerInput.getResponseBuilder().addElicitSlotDirective("menu", intentRequest.getIntent())
          .withSpeech("Wie lautet Ihr weiteres Gericht?").withReprompt("Was möchten Sie essen?").build();
    }

    return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

  }

}
