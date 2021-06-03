package com.tests;

import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Test;

import com.alexa.myThaiStar.MyThaiStarStreamHandler;
import com.alexa.myThaiStar.handlers.OrderInhouse.CloseOrderOrMakeServingTime;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.orderline.RequestOrder;
import com.tools.HelperOrderClass;

public class GetMyRequestHandlerTest {

  public static final String BASE_URL = MyThaiStarStreamHandler.BASE_URL;

  // @Test
  // public void bookATable() {
  //
  // RequestHandler requestHandler = new BookATable(BASE_URL);
  //
  // HandlerInput.Builder handlerInputBuilder = HandlerInput.builder();
  //
  // Intent intent = Intent.builder().withName("bookATable")
  // .putSlotsItem("date", Slot.builder().withName("AMAZON.DATE").withValue("2021-05-31").build())
  // .putSlotsItem("time", Slot.builder().withName("AMAZON.TIME").withValue("16:00").build())
  // .putSlotsItem("guests", Slot.builder().withName("AMAZON.FOUR_DIGIT_NUMBER").withValue("8").build()).build();
  //
  // Request request = IntentRequest.builder().withIntent(intent).build();
  //
  // RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(request).build();
  //
  // HandlerInput input = handlerInputBuilder.withRequestEnvelope(requestEnvelope).build();
  //
  // assertTrue(requestHandler.canHandle(input));
  //
  // Optional<Response> response = requestHandler.handle(input);
  // assertFalse(response.equals(null));
  //
  // }

  @Test
  public void orderHome() {

    IntentRequestHandler requestHandler = new CloseOrderOrMakeServingTime();

    // IntentRequest.Builder intentRequestBuilder = IntentRequest.builder();

    HandlerInput.Builder handlerInputBuilder = HandlerInput.builder();

    Intent intent = Intent.builder().withName("makeAOrderInhouse")
        .putSlotsItem("serveTimeYesNo", Slot.builder().withName("yesNo").withValue("nein").build()).build();

    Request request = IntentRequest.builder().withIntent(intent).build();

    RequestEnvelope requestEnvelope = RequestEnvelope.builder().withRequest(request).build();

    // IntentRequest intentRequest = intentRequestBuilder.withIntent(intent).build();

    HandlerInput input = handlerInputBuilder.withRequestEnvelope(requestEnvelope).build();

    HelperOrderClass.req = new RequestOrder();

    // assertTrue(requestHandler.canHandle(input));

    Optional<Response> response = requestHandler.handle(input);
    assertFalse(response.equals(null));

  }

}
