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
import com.tools.BasicOperations;

/**
 *
 * Regular order is finished. Single dishes can still be added
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

    Slot yesNoCustomerDetails = intentRequest.getIntent().getSlots().get("yesNoCustomerDetails");

    Map<String, Object> attributes = handlerInput.getAttributesManager().getSessionAttributes();

    String speechText = "";
    if (BasicOperations.previousOrder != null) {
      speechText = " Was Sie bisher bestellt haben: ";
      for (String s : BasicOperations.previousOrder) {
        speechText += s + " ,";
      }

      speechText.substring(0, speechText.length() - 1);

    }

    if (attributes.containsKey(Attributes.STATE_KEY_ONLY_ADD_INDIVIDUAL)) {

      return handlerInput.getResponseBuilder()
          .withSpeech("Vielen Dank. " + speechText
              + ". Sie können noch etwas hinzufügen oder die Bestellung einfach abschließen.")
          .withReprompt(
              "Sie können auch noch weitere Gerichte oder Getränke hinzufügen. Sagen Sie dazu zum Beispiel: Ich möchte noch Garlic Paradise Salad")
          .build();

    }

    if (attributes.containsValue(Attributes.START_STATE_WHERE_LIKE_TO_EAT_DELIVER)) {

      attributes.put(Attributes.STATE_KEY_ONLY_ADD_INDIVIDUAL, Attributes.START_STATE_ONLY_ADD_ONE);

      return handlerInput.getResponseBuilder().withSpeech("Vielen Dank. " + speechText
          + ". Wenn Sie die Bestellung abschließen möchten, dann sagen Sie: Bestellung abschließen. Ansonsten können Sie auch noch weitere Gerichte oder Getränke hinzufügen. Sagen Sie zum Beispiel: ich möchte noch Thai green chicken curry")
          .withReprompt(
              "Sie können auch noch weitere Gerichte oder Getränke hinzufügen. Sagen Sie dazu zum Beispiel: Ich möchte noch Garlic Paradise Salad")
          .build();
    }

    if (Attributes.START_STATE_COUNTER_BOOKING_IDS == 0)
      return handlerInput.getResponseBuilder()
          .withSpeech(
              "Keine Buchungs ID gefunden. Bitte buchen Sie zuerst einen Tisch, bevor Sie eine Bestellung vornehmen.")
          .build();

    if (yesNoCustomerDetails.getValue() != null)
      if (yesNoCustomerDetails.getValue().equals("nein"))
        return handlerInput.getResponseBuilder()
            .withSpeech("Es tut mir leid. Es gibt keine Auswahlmöglichkeit für eine spätere Reservierung.")
            .withShouldEndSession(true).build();

    attributes.put(Attributes.STATE_KEY_ONLY_ADD_INDIVIDUAL, Attributes.START_STATE_ONLY_ADD_ONE);
    return handlerInput.getResponseBuilder().withSpeech("Vielen Dank. Ihre Bestellung wurde aufgenommen. " + speechText
        + ". Wenn Sie die Bestellung abschließen möchten, dann sagen Sie: Bestellung abschließen. Ansonsten können Sie auch noch weitere Gerichte oder Getränke hinzufügen. Sagen Sie zum Beispiel: Ich möchte noch Thai green chicken curry.")
        .withReprompt(
            "Sie können auch noch weitere Gerichte oder Getränke hinzufügen. Sagen Sie dazu zum Beispiel: Ich möchte noch Garlic Paradise Salad")
        .build();
  }

}
