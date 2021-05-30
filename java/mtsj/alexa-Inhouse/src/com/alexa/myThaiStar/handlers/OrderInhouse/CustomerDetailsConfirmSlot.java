package com.alexa.myThaiStar.handlers.OrderInhouse;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Date;
import java.util.Optional;

import org.apache.http.message.BasicHeader;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.entity.booking.Booking;
import com.entity.booking.Content;
import com.entity.booking.ResponseBooking;
import com.entity.orderline.RequestOrder;
import com.google.gson.Gson;
import com.login.RequestLogin;
import com.tools.BasicOperations;
import com.tools.HelperOrderClass;

public class CustomerDetailsConfirmSlot implements IntentRequestHandler {

  private static String BASE_URL;

  private static long flexTime = 1800000;

  /**
   * The constructor.
   *
   * @param baseUrl
   */
  public CustomerDetailsConfirmSlot(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderInhouse"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS)
        && intentRequest.getIntent().getSlots().get("queryTable").getConfirmationStatusAsString().equals("NONE")
        && intentRequest.getIntent().getSlots().get("queryTable").getValue() != null;

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
      return handlerInput.getResponseBuilder().withSpeech(speechText).build();
    }

    ResponseBooking response = gson.fromJson(respStr, ResponseBooking.class);

    Booking booking = tableBooked(response, intentRequest);

    if (booking == null)
      return handlerInput.getResponseBuilder().addDelegateDirective(intentRequest.getIntent()).build();

    return handlerInput.getResponseBuilder().addConfirmSlotDirective("queryTable", intentRequest.getIntent())
        .withSpeech("Name: " + HelperOrderClass.req.booking.name + ". Anzahl der Gäste: "
            + HelperOrderClass.req.booking.assistants + ". Email: " + HelperOrderClass.req.booking.email
            + ". Sind Ihre Daten korrekt?")
        .withReprompt("Sind Ihre Daten korrekt?").build();
  }

  public Booking tableBooked(ResponseBooking response, IntentRequest intentRequest) {

    Slot queryTable = intentRequest.getIntent().getSlots().get("queryTable");
    Date date = new Date();
    long timeNow = date.getTime();

    for (Content c : response.content) {
      if (Integer.parseInt(c.booking.tableId) == Integer.parseInt(queryTable.getValue())
          && Math.abs((Long.parseLong(c.booking.bookingDate.replace(".000000000", "")) * 1000) - timeNow) <= flexTime) {

        HelperOrderClass.req = new RequestOrder();
        HelperOrderClass.req.booking.bookingToken = c.booking.bookingToken;
        HelperOrderClass.req.booking.name = c.booking.name;
        HelperOrderClass.req.booking.assistants = c.booking.assistants;
        HelperOrderClass.req.booking.email = c.booking.email;

        return c.booking;

      }

    }

    return null;
  }

}
