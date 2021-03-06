package com.alexa.myThaiStar.handlers.OrderHome;

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
import com.tools.BasicOperations;

/**
 *
 * Check the extras that the customer wants and choose the number of the dish
 *
 */
public class AmountDishesOrCorrectExtras implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("dishOrder").getValue() != null
        && intentRequest.getIntent().getSlots().get("extra").getValue() != null
        && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() == null
        && intentRequest.getIntent().getSlots().get("amount").getValue() == null;
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot extra = intentRequest.getIntent().getSlots().get("extra");

    // proceed without extras
    if (extra.getValue().equals("ohne extras"))
      return handlerInput.getResponseBuilder().addElicitSlotDirective("amount", intentRequest.getIntent())
          .withSpeech("Wie oft möchten Sie, dass von Ihnen ausgewählte Gericht bestellen ?").withReprompt("Wie oft ?")
          .build();

    ArrayList<Extras> extrasArray = new ArrayList<>();

    for (Extras s : BasicOperations.extras) {

      if (extra.getValue().contains(s.name.toLowerCase())) {
        Extras extras = new Extras();
        extras.id = s.id;
        extrasArray.add(extras);
      }

    }

    // specified extras do not exist for the dish
    if (extrasArray.size() == 0) {

      return handlerInput.getResponseBuilder().addElicitSlotDirective("extra", intentRequest.getIntent())
          .withSpeech("Ich habe sie leider nicht verstanden. Welche Extras möchten Sie ? "
              + BasicOperations.getExtrasName(BasicOperations.dishID))
          .withReprompt("Welche Extras möchten Sie?").build();

    }

    // how often would you like the dish
    return handlerInput.getResponseBuilder().addElicitSlotDirective("amount", intentRequest.getIntent())
        .withSpeech("Wie oft möchten Sie, dass von Ihnen ausgewählte Gericht bestellen ?").withReprompt("Wie oft ?")
        .build();
  }

}
