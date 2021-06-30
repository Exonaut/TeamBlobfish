/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.alexa.myThaiStar.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

public class HelpIntentHandler implements RequestHandler {

  @Override
  public boolean canHandle(HandlerInput input) {

    return input.matches(intentName("AMAZON.HelpIntent"));
  }

  @Override
  public Optional<Response> handle(HandlerInput input) {

    String speechText = "Wenn Sie wissen möchten was auf der Karte steht, dann sagen Sie zum Beispiel: Die Karte bitte"
        + ". Wenn Sie eine Bestellung durchführen möchten, dann sagen Sie zum Beispiel: Ich möchte etwas bestellen.";
    return input.getResponseBuilder().withSpeech(speechText).withReprompt(speechText).build();
  }
}
