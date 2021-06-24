package com.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import com.alexa.myThaiStar.MyThaiStarStreamHandler;
import com.alexa.myThaiStar.handlers.BookATable;
import com.alexa.myThaiStar.handlers.OrderHome.AmountDishes;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.tools.HelperOrderClass;

public class GetMyRequestHandlerTest {

  public static final String BASE_URL = MyThaiStarStreamHandler.BASE_URL;

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

  @Test
  public void orderHome() {

    IntentRequestHandler requestHandler = new AmountDishes();

    // IntentRequest.Builder intentRequestBuilder = IntentRequest.builder();

    HandlerInput.Builder handlerInputBuilder = HandlerInput.builder();

    Intent intent = Intent.builder().withName("bookATable")
        .putSlotsItem("extra", Slot.builder().withName("extras").withValue("boulette").build()).build();

    Request request = IntentRequest.builder().withIntent(intent).build();

    RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(request).build();

    // IntentRequest intentRequest = intentRequestBuilder.withIntent(intent).build();

    HandlerInput input = handlerInputBuilder.withRequestEnvelope(requestEnvelope).build();

    HelperOrderClass.getExtrasName("2");

    // assertTrue(requestHandler.canHandle(input));

    Optional<Response> response = requestHandler.handle(input);
    assertFalse(response.equals(null));

  }

}