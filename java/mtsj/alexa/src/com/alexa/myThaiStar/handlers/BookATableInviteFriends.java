package com.alexa.myThaiStar.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class BookATableInviteFriends implements RequestHandler {

  private static String BASE_URL;

  public BookATableInviteFriends(String baseUrl) {

    BASE_URL = baseUrl;
  }

  @Override
  public boolean canHandle(HandlerInput input) {

    return input.matches(intentName("inviteFriends"));
  }

  @Override
  public Optional<Response> handle(HandlerInput input) {

    // TODO Auto-generated method stub
    return null;
  }

}
