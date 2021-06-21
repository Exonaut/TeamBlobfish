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

import com.alexa.myThaiStar.handlers.CallFoodMenu;
import com.alexa.myThaiStar.handlers.CancelandStopIntentHandler;
import com.alexa.myThaiStar.handlers.DescriptionDishes;
import com.alexa.myThaiStar.handlers.DescriptionDrinks;
import com.alexa.myThaiStar.handlers.FallbackIntentHandler;
import com.alexa.myThaiStar.handlers.HelpIntentHandler;
import com.alexa.myThaiStar.handlers.LaunchRequestHandler;
import com.alexa.myThaiStar.handlers.SessionEndedRequestHandler;
import com.alexa.myThaiStar.handlers.OrderInhouse.AmountDishesOrCorrectExtras;
import com.alexa.myThaiStar.handlers.OrderInhouse.AmountDrinks;
import com.alexa.myThaiStar.handlers.OrderInhouse.AnotherDishOrDoUWantToDrink;
import com.alexa.myThaiStar.handlers.OrderInhouse.AnotherDishYesNoOrCorrectTheDish;
import com.alexa.myThaiStar.handlers.OrderInhouse.AnotherDrinkOrServeTimeYesNo;
import com.alexa.myThaiStar.handlers.OrderInhouse.AnotherDrinkYesNoOrCorrectTheDrink;
import com.alexa.myThaiStar.handlers.OrderInhouse.CloseOrder;
import com.alexa.myThaiStar.handlers.OrderInhouse.CloseOrderOrMakeServingTime;
import com.alexa.myThaiStar.handlers.OrderInhouse.Completed;
import com.alexa.myThaiStar.handlers.OrderInhouse.CustomerDetailsConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderInhouse.DishesConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderInhouse.DrinksConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderInhouse.QueryTable;
import com.alexa.myThaiStar.handlers.OrderInhouse.StartedOrder;
import com.alexa.myThaiStar.handlers.OrderInhouse.WhichExtrasDishes;
import com.alexa.myThaiStar.handlers.OrderInhouse.EatOrDrink;
import com.alexa.myThaiStar.handlers.OrderInhouse.WhatDoUWantDoDrinkOrServeTimeYesNo;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class MyThaiStarStreamHandler extends SkillStreamHandler {

  public static final String BASE_URL = "https://d54b4acfd60c.ngrok.io";

  private static Skill getSkill() {

    return Skills.standard()
        .addRequestHandlers(new CancelandStopIntentHandler(),
            new CallFoodMenu(),
            new DescriptionDrinks(BASE_URL),
            new DescriptionDishes(BASE_URL),
            new HelpIntentHandler(),
            new LaunchRequestHandler(),
            new AnotherDishYesNoOrCorrectTheDish(),
            new SessionEndedRequestHandler(),
            new FallbackIntentHandler(),
            new EatOrDrink(),
            new WhatDoUWantDoDrinkOrServeTimeYesNo(),
            new WhichExtrasDishes(),
            new DrinksConfirmSlot(),
            new DishesConfirmSlot(),
            new Completed(),
            new AnotherDrinkYesNoOrCorrectTheDrink(),
            new AnotherDrinkOrServeTimeYesNo(),
            new AnotherDishOrDoUWantToDrink(),
            new AmountDrinks(),
            new AmountDishesOrCorrectExtras(),
            new QueryTable(),
            new CustomerDetailsConfirmSlot(),
            new CloseOrderOrMakeServingTime(),
            new CloseOrder(),
            new StartedOrder())
        // Add your skill id below
        .withSkillId("amzn1.ask.skill.b8b902af-2c98-409c-92be-3d0ff9c3096f").build();
  }

  public MyThaiStarStreamHandler() {

    super(getSkill());
  }
}
