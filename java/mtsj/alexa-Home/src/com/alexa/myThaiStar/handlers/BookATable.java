
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
import com.entity.booking.Booking;
import com.entity.booking.RequestBooking;
import com.google.gson.Gson;
import com.tools.BasicOperations;
import com.tools.HelperOrderClass;

public class BookATable implements RequestHandler {

  private static String BASE_URL;

  /**
   * The constructor.
   *
   * @param baseUrl
   */
  public BookATable(String baseUrl) {

    BASE_URL = baseUrl;
  }

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

    Slot personCount = slots.get("guests");
    Slot time = slots.get("time");
    Slot date = slots.get("date");

    String date_time = HelperOrderClass.getFormatDateTimeAndCalculate(date.getValue() + " " + time.getValue());

    // commented out for unit tests
    // String name = input.getServiceClientFactory().getUpsService().getProfileName();
    // String userEmail = input.getServiceClientFactory().getUpsService().getProfileEmail();

    String name = "Tony";
    String userEmail = "tony2510@gmx.de";

    com.entity.booking.RequestBooking myApiRequest = new RequestBooking();
    myApiRequest.booking = new Booking();
    myApiRequest.booking.email = userEmail;
    myApiRequest.booking.assistants = personCount.getValue();
    myApiRequest.booking.bookingDate = date_time;
    myApiRequest.booking.name = name;
    myApiRequest.booking.bookingType = "0";

    BasicOperations bo = new BasicOperations();
    String speechText = "";
    Gson gson = new Gson();
    String payload = gson.toJson(myApiRequest);

    try {
      bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking");
    } catch (Exception ex) {
      speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
      return input.getResponseBuilder().withSpeech(speechText + "\n " + payload)
          .withSimpleCard("BookATable", speechText + " \n " + payload).build();
    }

    speechText = "Vielen Dank. Ihre Reservierung wurde aufgenommen. Wir freuen uns auf Ihren Besuch.";

    return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("BookATable", speechText).build();
  }

}
