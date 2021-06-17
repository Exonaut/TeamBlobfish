package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.model.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelpClass;

public class Completed implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.COMPLETED);
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot yesNoCustomerDetails = intentRequest.getIntent().getSlots().get("yesNoCustomerDetails");

    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    Slot whereLikeToEat = intentRequest.getIntent().getSlots().get("whereLikeToEat");
    if (attributes.containsValue(Attributes.START_STATE_WHERE_LIKE_TO_EAT_DELIVER)
        || (whereLikeToEat.getValue() != null && whereLikeToEat.getValue().equals("liefern"))) {

      String speechText = HelpClass.sendOrder();

      if (speechText == null)
        return handlerInput.getResponseBuilder()
            .withSpeech(
                "Es tut uns leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
            .build();

      return handlerInput.getResponseBuilder()
          .withSpeech(speechText + " Wir werden Ihre Bestellung schnellstmöglich liefern lassen.")
          .withShouldEndSession(true).build();
    }

    if (HelpClass.counterBookingIDs == 0)
      return handlerInput.getResponseBuilder()
          .withSpeech(
              "Keine Buchungs ID gefunden. Bitte buchen Sie zuerst einen Tisch, bevor Sie eine Bestellung vornehmen.")
          .withShouldEndSession(true).build();

    if (yesNoCustomerDetails.getValue() != null)
      if (yesNoCustomerDetails.getValue().equals("nein"))
        return handlerInput.getResponseBuilder()
            .withSpeech("Es tut mir leid. Es gibt keine Auswahlmöglichkeit für eine spätere Reservierung.")
            .withShouldEndSession(true).build();

    String speechText = HelpClass.sendOrder();
    if (speechText == null)
      return handlerInput.getResponseBuilder()
          .withSpeech("Es tut uns leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
          .withShouldEndSession(true).build();

    return handlerInput.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true).build();
  }

}
