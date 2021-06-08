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
import com.alexa.myThaiStar.handlers.OrderHome.AdressConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderHome.AmountDishesOrCorrectExtras;
import com.alexa.myThaiStar.handlers.OrderHome.AmountDrinks;
import com.alexa.myThaiStar.handlers.OrderHome.AnotherDishOrDoUWantToDrink;
import com.alexa.myThaiStar.handlers.OrderHome.AnotherDishYesNoOrCorrectTheDish;
import com.alexa.myThaiStar.handlers.OrderHome.AnotherDrinkOrEnterTheAdressCityOrMakeServingTime;
import com.alexa.myThaiStar.handlers.OrderHome.AnotherDrinkYesNoOrCorrectTheDrink;
import com.alexa.myThaiStar.handlers.OrderHome.CloseDeliveryServiceOrCorrectTheAdress;
import com.alexa.myThaiStar.handlers.OrderHome.CloseOrderOrCorrectTheServeTime;
import com.alexa.myThaiStar.handlers.OrderHome.Completed;
import com.alexa.myThaiStar.handlers.OrderHome.DeliveryServiceOrCheckCustomerDetails;
import com.alexa.myThaiStar.handlers.OrderHome.DishesConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderHome.DrinksConfirmSlot;
import com.alexa.myThaiStar.handlers.OrderHome.WhichExtrasDishes;
import com.alexa.myThaiStar.handlers.OrderHome.StartedOrderOrClose;
import com.alexa.myThaiStar.handlers.OrderHome.WhatDoUWantDoDrinkOrEnterTheAdressCityOrServeTime;
import com.alexa.myThaiStar.handlers.OrderHome.WhereLikeToEat;
import com.alexa.myThaiStar.handlers.OrderHome.WhichStreet;
import com.alexa.myThaiStar.handlers.OrderHome.WhichStreetNumber;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

public class MyThaiStarStreamHandler extends SkillStreamHandler {

  public static final String BASE_URL = "https://a0681af777e4.ngrok.io";

  private static Skill getSkill() {

    return Skills.standard()
        .addRequestHandlers(new CancelandStopIntentHandler(),
            new BookATable(),
            new CallFoodMenu(),
            new DescriptionDrinks(BASE_URL),
            new DescriptionDishes(BASE_URL),
            new StartedOrderOrClose(),
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
            new AnotherDrinkOrEnterTheAdressCityOrMakeServingTime(),
            new WhatDoUWantDoDrinkOrEnterTheAdressCityOrServeTime (),
            new CloseOrderOrCorrectTheServeTime(),
            new WhereLikeToEat(),
            new DeliveryServiceOrCheckCustomerDetails(),
            new WhichStreet(),
            new AdressConfirmSlot(),
            new CloseDeliveryServiceOrCorrectTheAdress(),
            new WhichStreetNumber()
            )
        // Add your skill id below
        .withSkillId("amzn1.ask.skill.2cb6b023-f36d-43f9-8c18-3b509cf3f2d6").build();
  }

  public MyThaiStarStreamHandler() {

    super(getSkill());
  }
}
