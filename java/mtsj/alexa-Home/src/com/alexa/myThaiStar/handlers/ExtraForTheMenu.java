package com.alexa.myThaiStar.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.alexa.myThaiStar.model.Attributes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelpClass;

public class ExtraForTheMenu implements IntentRequestHandler {

  @Override
  public boolean canHandle(HandlerInput input, IntentRequest intentRequest) {

    return input.matches(intentName("extraForTheMenu")) || (input.matches(intentName("extraForTheMenu"))
        && input.getAttributesManager().getSessionAttributes().containsKey(Attributes.STATE_KEY_MENU));
  }

  @Override
  public Optional<Response> handle(HandlerInput input, IntentRequest intentRequest) {

    Intent intent = intentRequest.getIntent();
    Map<String, Slot> slots = intent.getSlots();
    Slot dishName = slots.get("dishName");

    String dishId = HelpClass.getDishId(dishName.getValue());

    if (dishId == null) {
      return input.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent()).withSpeech(
          "Es tut mir leid, dieses Gericht haben wir leider nicht auf der Speisekarte. Bitte wählen Sie ein Gericht aus, welches auf der Speisekarte vorhanden ist.")
          .withReprompt("Zu welchem Gericht möchten Sie die extras erfahren?").build();
    }

    String extras = HelpClass.getExtrasName(dishId);

    if (extras == null) {
      return input.getResponseBuilder()
          .withSpeech("Es tut uns leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt.")
          .build();
    }

    return input.getResponseBuilder().withSpeech(extras).build();

  }

}
