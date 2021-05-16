/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.alexa.myThaiStar;

import com.alexa.myThaiStar.handlers.BookATable;
import com.alexa.myThaiStar.handlers.CallFoodMenu;
import com.alexa.myThaiStar.handlers.CancelandStopIntentHandler;
import com.alexa.myThaiStar.handlers.DescriptionDrinks;
import com.alexa.myThaiStar.handlers.DescriptionDishes;
import com.alexa.myThaiStar.handlers.FallbackIntentHandler;
import com.alexa.myThaiStar.handlers.HelpIntentHandler;
import com.alexa.myThaiStar.handlers.LaunchRequestHandler;
import com.alexa.myThaiStar.handlers.SessionEndedRequestHandler;
import com.alexa.myThaiStar.handlers.Order.MakeAOrderHome;
import com.alexa.myThaiStar.handlers.Order.MenuThreeSlot;
import com.alexa.myThaiStar.handlers.Order.MenuTwoSlot;
import com.alexa.myThaiStar.handlers.Order.YesNoSlotOne;
import com.alexa.myThaiStar.handlers.Order.YesNoSlotThree;
import com.alexa.myThaiStar.handlers.Order.YesNoSlotTwo;
import com.alexa.myThaiStar.handlers.Order.AmountOneGivenConfirmSlot;
import com.alexa.myThaiStar.handlers.Order.AmountOneSlot;
import com.alexa.myThaiStar.handlers.Order.AmountThreeGivenConfirmSlot;
import com.alexa.myThaiStar.handlers.Order.AmountThreeSlot;
import com.alexa.myThaiStar.handlers.Order.AmountTwoGivenConfirmSlot;
import com.alexa.myThaiStar.handlers.Order.AmountTwoSlot;
import com.alexa.myThaiStar.handlers.Order.Completed;
import com.alexa.myThaiStar.handlers.Order.ExtraOneSlot;
import com.alexa.myThaiStar.handlers.Order.ExtraThreeSlot;
import com.alexa.myThaiStar.handlers.Order.ExtraTwoSlot;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class MyThaiStarStreamHandler extends SkillStreamHandler {

  public static final String BASE_URL = "https://fe5ab513ff21.ngrok.io";

  private static Skill getSkill() {

    return Skills.standard()
        .addRequestHandlers(new CancelandStopIntentHandler(),
            new BookATable(BASE_URL),
            new CallFoodMenu(BASE_URL),
            new DescriptionDrinks(BASE_URL),
            new DescriptionDishes(BASE_URL),
            new MakeAOrderHome(BASE_URL),
            new ExtraOneSlot(BASE_URL),
            new YesNoSlotOne(BASE_URL),
            new HelpIntentHandler(),
            new LaunchRequestHandler(),
            new SessionEndedRequestHandler(),
            new FallbackIntentHandler(),
            new Completed(BASE_URL),
            new AmountOneSlot(BASE_URL),
            new AmountOneGivenConfirmSlot (BASE_URL),
            new AmountTwoGivenConfirmSlot (BASE_URL),
            new AmountThreeGivenConfirmSlot (BASE_URL),
            new YesNoSlotTwo(BASE_URL),
            new YesNoSlotThree(BASE_URL),
            new MenuTwoSlot(BASE_URL),
            new MenuThreeSlot(BASE_URL),
            new ExtraTwoSlot(BASE_URL),
            new AmountTwoSlot(BASE_URL),
            new AmountThreeSlot(BASE_URL),
            new ExtraThreeSlot(BASE_URL))

        // Add your skill id below
        .withSkillId("amzn1.ask.skill.2cb6b023-f36d-43f9-8c18-3b509cf3f2d6").build();
  }

  public MyThaiStarStreamHandler() {

    super(getSkill());
  }
}
