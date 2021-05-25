package com.tools;

import java.util.ArrayList;

import com.entity.orderline.Extras;
import com.entity.orderline.OrderLines;

public class Test {

  public static String BASE_URL = "https://08d2b6edb3e5.ngrok.io";

  public static void main(String[] args) {

    String menu1 = "thai green chicken curry";
    String menu2 = "thai Peanut Beef";

    String extra1 = "tofu und extra curry";
    String extra2 = "tofu";

    test(menu1, extra1);
    test(menu2, extra2);

    System.out.println(HelperOrderClass.sendOrder());

  }

  public static void test(String menu, String extra) {

    OrderLines tmp = new OrderLines();
    ArrayList<Extras> extrasArray = new ArrayList<>();
    ArrayList<String> extrasNameArray = HelperOrderClass.getExtrasNameArray();

    for (String s : extrasNameArray) {

      if (extra.contains(s.toLowerCase())) {
        Extras extras = new Extras();
        extras.id = HelperOrderClass.getExtrasID(s.toLowerCase());
        extrasArray.add(extras);
      }

    }

    tmp.extras.addAll(extrasArray);
    tmp.orderLine.amount = "2";
    tmp.orderLine.dishId = HelperOrderClass.getDishId(menu);
    tmp.orderLine.comment = "";

    HelperOrderClass.req.orderLines.add(tmp);
    HelperOrderClass.req.booking.bookingToken = "CB_20210516_da0503fd42d6a8349fe8bfc988312c82";

  }

}
