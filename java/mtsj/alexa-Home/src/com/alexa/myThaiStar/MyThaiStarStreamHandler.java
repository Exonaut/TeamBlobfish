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
import com.alexa.myThaiStar.handlers.DescriptionDishes;
import com.alexa.myThaiStar.handlers.DescriptionDrinks;
import com.alexa.myThaiStar.handlers.FallbackIntentHandler;
import com.alexa.myThaiStar.handlers.HelpIntentHandler;
import com.alexa.myThaiStar.handlers.LaunchRequestHandler;
import com.alexa.myThaiStar.handlers.SessionEndedRequestHandler;
import com.alexa.myThaiStar.handlers.OrderHome.HomeAmountDishes;
import com.alexa.myThaiStar.handlers.OrderHome.HomeAmountDrinks;
import com.alexa.myThaiStar.handlers.OrderHome.HomeAnotherDishOrDoUWantToDrink;
import com.alexa.myThaiStar.handlers.OrderHome.HomeAnotherDishYesNo;
import com.alexa.myThaiStar.handlers.OrderHome.HomeAnotherDrinkOrDoUWantCloseOrder;
import com.alexa.myThaiStar.handlers.OrderHome.HomeAnotherDrinkYesNo;
import com.alexa.myThaiStar.handlers.OrderHome.HomeCompleted;
import com.alexa.myThaiStar.handlers.OrderHome.HomeDishesConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderHome.HomeDrinksConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderHome.HomeShowExtrasDishes;
import com.alexa.myThaiStar.handlers.OrderHome.HomeStartedOrder;
import com.alexa.myThaiStar.handlers.OrderHome.HomeWhatDoUWantDoDrink;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class MyThaiStarStreamHandler extends SkillStreamHandler {

  public static final String BASE_URL = "https://9c46483b9dc0.ngrok.io";

  private static Skill getSkill() {

    return Skills.standard()
        .addRequestHandlers(new CancelandStopIntentHandler(),
            new BookATable(BASE_URL),
            new CallFoodMenu(BASE_URL),
            new DescriptionDrinks(BASE_URL),
            new DescriptionDishes(BASE_URL),
            new HomeStartedOrder(BASE_URL),
            new HomeShowExtrasDishes(),
            new HomeAnotherDishYesNo(),
            new HomeAnotherDrinkYesNo(),
            new HelpIntentHandler(),
            new LaunchRequestHandler(),
            new SessionEndedRequestHandler(),
            new FallbackIntentHandler(),
            new HomeCompleted(),
            new HomeAmountDishes(),
            new HomeAmountDrinks(),
            new HomeDishesConfirmSlot(),
            new HomeDrinksConfirmSlot(),
            new HomeAnotherDishOrDoUWantToDrink(),
            new HomeAnotherDrinkOrDoUWantCloseOrder(),
            new HomeWhatDoUWantDoDrink ())
        // Add your skill id below
        .withSkillId("amzn1.ask.skill.2cb6b023-f36d-43f9-8c18-3b509cf3f2d6").build();
  }

  public MyThaiStarStreamHandler() {

    super(getSkill());
  }
}
