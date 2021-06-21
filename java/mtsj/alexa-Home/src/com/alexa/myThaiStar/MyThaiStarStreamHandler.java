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
import com.alexa.myThaiStar.handlers.orderHome.AmountDishesOrCorrectExtras;
import com.alexa.myThaiStar.handlers.orderHome.AmountDrinks;
import com.alexa.myThaiStar.handlers.orderHome.AnotherDishOrDoUWantToDrink;
import com.alexa.myThaiStar.handlers.orderHome.AnotherDishYesNoOrCorrectTheDish;
import com.alexa.myThaiStar.handlers.orderHome.AnotherDrinkOrMakeServingTimeOrCloseOrder;
import com.alexa.myThaiStar.handlers.orderHome.AnotherDrinkYesNoOrCorrectTheDrink;
import com.alexa.myThaiStar.handlers.orderHome.CloseOrderOrCorrectTheServeTime;
import com.alexa.myThaiStar.handlers.orderHome.Completed;
import com.alexa.myThaiStar.handlers.orderHome.DishesConfirmSlot;
import com.alexa.myThaiStar.handlers.orderHome.DrinksConfirmSlot;
import com.alexa.myThaiStar.handlers.orderHome.EatOrDrinkOrCheckCustomerDetails;
import com.alexa.myThaiStar.handlers.orderHome.EatOrDrinkOrClose;
import com.alexa.myThaiStar.handlers.orderHome.StartEatOrDrink;
import com.alexa.myThaiStar.handlers.orderHome.WhatDoUWantToDrinkOrServeTimeOrCloseOrder;
import com.alexa.myThaiStar.handlers.orderHome.WhereLikeToEat;
import com.alexa.myThaiStar.handlers.orderHome.WhichExtrasDishes;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class MyThaiStarStreamHandler extends SkillStreamHandler {

  public static final String BASE_URL = "https://a0d219ab0b63.ngrok.io";

  private static Skill getSkill() {

    return Skills.standard()
        .addRequestHandlers(new CancelandStopIntentHandler(),
            new BookATable(),
            new CallFoodMenu(),
            new DescriptionDrinks(BASE_URL),
            new DescriptionDishes(BASE_URL),
            new EatOrDrinkOrClose(),
            new WhichExtrasDishes(),
            new AnotherDishYesNoOrCorrectTheDish(),
            new AnotherDrinkYesNoOrCorrectTheDrink(),
            new HelpIntentHandler(),
            new LaunchRequestHandler(),
            new SessionEndedRequestHandler(),
            new FallbackIntentHandler(),
            new Completed(),
            new AmountDishesOrCorrectExtras(),
            new AmountDrinks(),
            new DishesConfirmSlot(),
            new DrinksConfirmSlot(),
            new AnotherDishOrDoUWantToDrink(),
            new AnotherDrinkOrMakeServingTimeOrCloseOrder(),
            new WhatDoUWantToDrinkOrServeTimeOrCloseOrder (),
            new CloseOrderOrCorrectTheServeTime(),
            new WhereLikeToEat(),
            new EatOrDrinkOrCheckCustomerDetails(),
            new StartEatOrDrink()
            )
        // Add your skill id below
        .withSkillId("amzn1.ask.skill.2cb6b023-f36d-43f9-8c18-3b509cf3f2d6").build();
  }

  public MyThaiStarStreamHandler() {

    super(getSkill());
  }
}
