/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.alexa.helloworld.handlers;

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
import com.entity.booking.Request;
import com.google.gson.Gson;
import com.tools.BasicOperations;

public class HelloWorldIntentHandler implements RequestHandler {

  public static final String BASE_URL = "https://6b20c194dffe.ngrok.io";

  @Override
  public boolean canHandle(HandlerInput input) {

    return input.matches(intentName("HelloWorldIntent"));
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

    // String name = input.getServiceClientFactory().getUpsService().getProfileName();
    // String userEmail = input.getServiceClientFactory().getUpsService().getProfileEmail();

    String date_time = date.getValue() + "T" + time.getValue() + ":00Z";

    Request myApiRequest = new Request();
    myApiRequest.booking = new Booking();
    myApiRequest.booking.email = "tony2510@gmx.de";
    myApiRequest.booking.assistants = personCount.getValue();
    myApiRequest.booking.bookingDate = date_time;
    myApiRequest.booking.name = "Tony";

    BasicOperations bo = new BasicOperations();
    String speechText = "";
    Gson gson = new Gson();
    String payload = gson.toJson(myApiRequest);

    try {
      bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking");
    } catch (Exception ex) {
      speechText = "Es ist ein Fehler bei MyThaiStar aufgetreten !";
      return input.getResponseBuilder().withSpeech(speechText + "\n " + payload)
          .withSimpleCard("HelloWorld", speechText + " \n " + payload).build();
    }

    speechText = "Ihre Buchung wurde abgeschlossen";

    return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("HelloWorld", speechText).build();
  }

}