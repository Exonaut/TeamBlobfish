/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.alexa.helloworld;

import com.alexa.helloworld.handlers.BookATable;
import com.alexa.helloworld.handlers.CallFoodMenu;
import com.alexa.helloworld.handlers.CancelandStopIntentHandler;
import com.alexa.helloworld.handlers.FallbackIntentHandler;
import com.alexa.helloworld.handlers.HelpIntentHandler;
import com.alexa.helloworld.handlers.LaunchRequestHandler;
import com.alexa.helloworld.handlers.SessionEndedRequestHandler;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class HelloWorldStreamHandler extends SkillStreamHandler {

  public static final String BASE_URL = "https://c39a544e5190.ngrok.io";

  private static Skill getSkill() {

    return Skills.standard()
        .addRequestHandlers(new CancelandStopIntentHandler(),
            new BookATable(BASE_URL),
            new CallFoodMenu(BASE_URL),
            new HelpIntentHandler(),
            new LaunchRequestHandler(),
            new SessionEndedRequestHandler(),
            new FallbackIntentHandler())
        // Add your skill id below
        .withSkillId("amzn1.ask.skill.2cb6b023-f36d-43f9-8c18-3b509cf3f2d6").build();
  }

  public HelloWorldStreamHandler() {

    super(getSkill());
  }

}
