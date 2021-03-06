package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.BasicOperations;

/**
 *
 * Choose extras to the dish.
 *
 */
public class WhichExtrasDishes implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return handlerInput.matches(intentName("makeAOrderHome"))
        && (intentRequest.getDialogState() == DialogState.IN_PROGRESS
            && intentRequest.getIntent().getSlots().get("dishOrder").getValue() != null
            && intentRequest.getIntent().getSlots().get("extra").getValue() == null
            && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() == null)
        || (intentRequest.getDialogState() == DialogState.STARTED
            && handlerInput.getAttributesManager().getSessionAttributes().containsKey("state") // if startet order
                                                                                               // information already
                                                                                               // saved. for example
                                                                                               // checking the booking
                                                                                               // id
            && intentRequest.getIntent().getSlots().get("dishOrder").getValue() != null
            && intentRequest.getIntent().getSlots().get("extra").getValue() == null
            && intentRequest.getIntent().getSlots().get("yesNoEat").getValue() == null);

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot dish = intentRequest.getIntent().getSlots().get("dishOrder");

    BasicOperations.dishID = BasicOperations.getDishId(dish.getValue());

    if (BasicOperations.dishID == null) // Dish does not exist
      return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
          .withSpeech(
              "Es tut mir leid, dieses Gericht haben wir leider nicht auf der Speisekarte. Bitte w??hlen Sie ein Gericht aus, welches auf der Speisekarte vorhanden ist.")
          .withReprompt("Welches Gericht m??chten Sie?").build();

    String speechTextExtras = BasicOperations.getExtrasName(BasicOperations.dishID); // store extras in a string

    // Connection problem
    if (speechTextExtras == null)
      return handlerInput.getResponseBuilder()
          .withSpeech("Es tut uns leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem sp??teren Zeitpunkt.")
          .build();

    // choose extras
    return handlerInput.getResponseBuilder().addElicitSlotDirective("extra", intentRequest.getIntent())
        .withSpeech(speechTextExtras + " Wenn Sie keine Extras m??chten, dann sagen Sie: ohne extras.")
        .withReprompt("Welche Extras m??chten Sie?").build();

  }

}
