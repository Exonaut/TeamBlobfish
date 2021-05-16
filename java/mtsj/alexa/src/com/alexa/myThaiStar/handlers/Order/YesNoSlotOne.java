package com.alexa.myThaiStar.handlers.Order;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.ArrayList;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.entity.orderline.Extras;
import com.entity.orderline.OrderLines;
import com.entity.orderline.RequestOrder;

public class YesNoSlotOne implements IntentRequestHandler {

  public static String BASE_URL;

  public static RequestOrder req = new RequestOrder();

  public YesNoSlotOne(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && !intentRequest.getIntent().getSlots().get("amountOne").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("yesNoOne").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("menuTwo").getValue() == null;

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    OrderLines tmp = new OrderLines();
    ArrayList<Extras> extras = new ArrayList<Extras>();

    extras.add(null)

    DataFromDatabase.req.orderLines.add(tmp.extras.a);

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("yesNoOne", intentRequest.getIntent())
        .withSpeech("Möchten Sie noch etwas zum essen bestellen?").withReprompt("Darf es noch etwa sein?").build();

  }

}
