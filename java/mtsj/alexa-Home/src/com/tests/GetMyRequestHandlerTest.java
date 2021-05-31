package com.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.alexa.myThaiStar.handlers.BookATable;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class GetMyRequestHandlerTest {

  public static final String BASE_URL = "https://61d3565eeded.ngrok.io";

  @Test
  public void bookATable() {

    RequestHandler requestHandler = new BookATable(BASE_URL);

    HandlerInput.Builder handlerInputBuilder = HandlerInput.builder();

    Intent intent = Intent.builder().withName("bookATable")
        .putSlotsItem("date", Slot.builder().withName("AMAZON.DATE").withValue("2021-05-31").build())
        .putSlotsItem("time", Slot.builder().withName("AMAZON.TIME").withValue("16:00").build())
        .putSlotsItem("guests", Slot.builder().withName("AMAZON.FOUR_DIGIT_NUMBER").withValue("8").build()).build();

    Request request = IntentRequest.builder().withIntent(intent).build();

    RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(request).build();

    HandlerInput input = handlerInputBuilder.withRequestEnvelope(requestEnvelope).build();

    assertTrue(requestHandler.canHandle(input));

    Optional<Response> response = requestHandler.handle(input);
    assertFalse(response.equals(null));

  }

}
