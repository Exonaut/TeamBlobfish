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
import com.alexa.myThaiStar.handlers.OrderHome.AmountDishes;
import com.alexa.myThaiStar.handlers.OrderHome.AmountDrinks;
import com.alexa.myThaiStar.handlers.OrderHome.AnotherDishOrDoUWantToDrink;
import com.alexa.myThaiStar.handlers.OrderHome.AnotherDishYesNo;
import com.alexa.myThaiStar.handlers.OrderHome.AnotherDrinkOrDeliveryService;
import com.alexa.myThaiStar.handlers.OrderHome.AnotherDrinkYesNo;
import com.alexa.myThaiStar.handlers.OrderHome.CloseOrder;
import com.alexa.myThaiStar.handlers.OrderHome.CloseOrderOrServingTime;
import com.alexa.myThaiStar.handlers.OrderHome.Completed;
import com.alexa.myThaiStar.handlers.OrderHome.DishesConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderHome.DrinksConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderHome.ShowExtrasDishes;
import com.alexa.myThaiStar.handlers.OrderHome.StartedOrder;
import com.alexa.myThaiStar.handlers.OrderHome.WhatDoUWantDoDrinkOrUwantDeliveryService;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class MyThaiStarStreamHandler extends SkillStreamHandler {

  public static final String BASE_URL = "https://4b8f873e8877.ngrok.io";

  private static Skill getSkill() {

    return Skills.standard()
        .addRequestHandlers(new CancelandStopIntentHandler(),
            new BookATable(BASE_URL),
            new CallFoodMenu(BASE_URL),
            new DescriptionDrinks(BASE_URL),
            new DescriptionDishes(BASE_URL),
            new StartedOrder(BASE_URL),
            new ShowExtrasDishes(),
            new AnotherDishYesNo(),
            new AnotherDrinkYesNo(),
            new HelpIntentHandler(),
            new LaunchRequestHandler(),
            new SessionEndedRequestHandler(),
            new FallbackIntentHandler(),
            new Completed(),
            new AmountDishes(),
            new AmountDrinks(),
            new DishesConfirmSlot(),
            new DrinksConfirmSlot(),
            new AnotherDishOrDoUWantToDrink(),
            new AnotherDrinkOrDeliveryService(),
            new WhatDoUWantDoDrinkOrUwantDeliveryService (),
            new CloseOrderOrServingTime(),
            new CloseOrder()
            )
        // Add your skill id below
        .withSkillId("amzn1.ask.skill.2cb6b023-f36d-43f9-8c18-3b509cf3f2d6").build();
  }

  public MyThaiStarStreamHandler() {

    super(getSkill());
  }
}
