package com.alexa.myThaiStar.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.DialogState;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class MakeAOrderHomeInProgress implements IntentRequestHandler {

  public static String BASE_URL;

  public MakeAOrderHomeInProgress(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput handlerInput, IntentRequest intentRequest) {

    return (handlerInput.matches(intentName("makeAOrderHome"))
        && intentRequest.getDialogState() == DialogState.IN_PROGRESS);
  }

  @Override
  public Optional<Response> handle(HandlerInput handlerInput, IntentRequest intentRequest) {

    Slot menuTwo = intentRequest.getIntent().getSlots().get("menuTwo");
    Slot extras = intentRequest.getIntent().getSlots().get("extra");

    if (extras.getValue() == null) {
      return handlerInput.getResponseBuilder().withSpeech("Möchten Sie extras ?")
          .addElicitSlotDirective("extra", intentRequest.getIntent()).build();
    } else if (extras.getValue().equals("nein")) {

      return handlerInput.getResponseBuilder().withSpeech("Ende").addDelegateDirective(intentRequest.getIntent())
          .build();
    }

    // if (intentRequest.getIntent().getSlots().get("extra") != null
    // && intentRequest.getIntent().getSlots().get("extra").getValue().equals("nein")) {
    //
    // return handlerInput.getResponseBuilder().withSpeech("Ende").withReprompt("Was möchten Sie für extras?").build();
    //
    // }

    return handlerInput.getResponseBuilder().withSpeech("jahaha").withReprompt("Wasn nun")
        .addDelegateDirective(intentRequest.getIntent()).build();
  }

}
