package com.alexa.myThaiStar.handlers.OrderHome;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import org.apache.http.message.BasicHeader;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.entity.booking.Content;
import com.entity.booking.ResponseBooking;
import com.entity.orderline.RequestOrder;
import com.google.gson.Gson;
import com.login.RequestLogin;
import com.tools.BasicOperations;
import com.tools.HelperOrderClass;

public class StartedOrder implements IntentRequestHandler {

  private static String BASE_URL;

  /**
   * The constructor.
   *
   * @param baseUrl
   */
  public StartedOrder(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.STARTED);

  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    RequestLogin req = new RequestLogin();
    req.password = "waiter";
    req.username = "waiter";
    Gson gson = new Gson();
    String payload = gson.toJson(req);
    String speechText = "";
    String respStr = "";
    BasicOperations bo = new BasicOperations();

    try {
      bo.basicPost(payload, BASE_URL + "/mythaistar/login");
    } catch (Exception ex) {
      speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
      return handlerInput.getResponseBuilder().withSpeech(speechText).withSimpleCard("BookATable", speechText).build();
    }

    String authorizationBearer = bo.getSpecificHeader("Authorization");
    payload = "{\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[]}}";

    BasicOperations bo2 = new BasicOperations();
    bo2.reqHeaders = new BasicHeader[] { new BasicHeader("Authorization", authorizationBearer) };

    try {
      respStr = bo2.basicPost(payload, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking/search");
    } catch (Exception ex) {
      speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
      return handlerInput.getResponseBuilder().withSpeech(speechText).withSimpleCard("BookATable", speechText).build();
    }

    ResponseBooking response = gson.fromJson(respStr, ResponseBooking.class);
    if (!bookingIDAvailable(response, handlerInput)) {
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();
    }

    return handlerInput.getResponseBuilder().addElicitSlotDirective("dishOrder", intentRequest.getIntent())
        .withSpeech("Wie lautet Ihr erstes Gericht?").withReprompt("Was möchten Sie essen?").build();
  }

  public boolean bookingIDAvailable(ResponseBooking response, HandlerInput handlerInput) {

    String userEmail = handlerInput.getServiceClientFactory().getUpsService().getProfileEmail();

    for (Content c : response.content) {

      if (c.booking.email.equals(userEmail)) {

        HelperOrderClass.req = new RequestOrder();

        HelperOrderClass.req.booking.bookingToken = c.booking.bookingToken;
        return true;
      }
    }

    return false;
  }
}
