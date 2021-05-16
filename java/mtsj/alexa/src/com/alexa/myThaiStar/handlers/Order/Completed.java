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

/**
 * TODO Spielecke This type ...
 *
 */
public class Completed implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.COMPLETED);
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    if (intentRequest.getIntent().getSlots().get("menu").getValue().equals("nichts"))
      return handlerInput.getResponseBuilder()
          .withSpeech(
              "Keine Buchungs ID gefunden. Bitte buchen Sie zuerst einen Tisch, bevor Sie eine Bestellung vornehmen.")
          .build();


    Slot menu = intentRequest.getIntent().getSlots().get("menu");

    OrderLines tmp = new OrderLines();
    ArrayList<Extras> extras = new ArrayList<Extras>();

    Extras extra = new Extras();

    extra.id = DataFromToDatabase.getExtrasID(intentRequest.getIntent().getSlots().get("extra").getValue());

    extras.add(extra);

    tmp.extras.addAll(extras);

    tmp.orderLine.amount = intentRequest.getIntent().getSlots().get("amount").getValue();
    tmp.orderLine.dishId = DataFromToDatabase.getDishId(menu.getValue());
    tmp.orderLine.comment = "";

    DataFromToDatabase.req.orderLines.add(tmp);

    String speechText = DataFromToDatabase.sendOrder();

    return handlerInput.getResponseBuilder().withSpeech(speechText).build();
  }

}
