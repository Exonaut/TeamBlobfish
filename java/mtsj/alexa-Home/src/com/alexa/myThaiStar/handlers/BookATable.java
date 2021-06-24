
package com.alexa.myThaiStar.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelpClass;

public class BookATable implements RequestHandler {

  @Override
  public boolean canHandle(HandlerInput input) {

    return input.matches(intentName("bookATable"));
  }

  @Override
  public Optional<Response> handle(HandlerInput input) {

    com.amazon.ask.model.Request request = input.getRequestEnvelope().getRequest();
    IntentRequest intentRequest = (IntentRequest) request;
    Intent intent = intentRequest.getIntent();
    Map<String, Slot> slots = intent.getSlots();
    String speechText = "Vielen Dank für Ihre Reservierung. Wir freuen uns auf Ihren Besuch.";

    Slot personCount = slots.get("guests");
    Slot time = slots.get("time");
    Slot date = slots.get("date");

    String date_time = HelpClass.getFormatDateTimeAndSubtractTwoHours(date.getValue() + " " + time.getValue());
    String name = input.getServiceClientFactory().getUpsService().getProfileName();
    String userEmail = input.getServiceClientFactory().getUpsService().getProfileEmail();
    String bookingType = "1";

    String response = HelpClass.bookATable(userEmail, name, date_time, personCount.getValue(), bookingType);

    if (response == null) {

      speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Probieren Sie es zu einem späteren Zeitpunkt.";

    }

    return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("BookATable", speechText)
        .withShouldEndSession(false).build();
  }

}
