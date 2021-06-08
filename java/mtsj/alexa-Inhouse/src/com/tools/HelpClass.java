package com.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.message.BasicHeader;

import com.alexa.myThaiStar.MyThaiStarStreamHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import com.entity.booking.Booking;
import com.entity.booking.ResponseBooking;
import com.entity.dish.Content;
import com.entity.dish.ResponseDescriptionDishes;
import com.entity.dish.ResponseMenuDishes;
import com.entity.orderline.Extras;
import com.entity.orderline.OrderLines;
import com.entity.orderline.RequestOrder;
import com.google.gson.Gson;
import com.login.RequestLogin;

public class HelpClass {

  public static String BASE_URL = MyThaiStarStreamHandler.BASE_URL;

  public static RequestOrder req;

  public static ArrayList<Extras> extras;

  public static String dishID;

  public static String callFoodMenu(String payload) {

    String resStr;

    BasicOperations bo = new BasicOperations();

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      return null;
    }

    return resStr;
  }

  public static String convertMillisecondsToDateTime(long milliSeconds) {

    Date formatDateTime = new Date(milliSeconds);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    return sdf.format(formatDateTime);
  }

  public static ResponseBooking getAllBookings() {

    RequestLogin req = new RequestLogin();
    req.password = "waiter";
    req.username = "waiter";
    Gson gson = new Gson();
    String payload = gson.toJson(req);
    String respStr = "";
    BasicOperations bo = new BasicOperations();

    try {
      bo.basicPost(payload, BASE_URL + "/mythaistar/login");
    } catch (Exception ex) {
      return null;
    }

    String authorizationBearer = bo.getSpecificHeader("Authorization");
    payload = "{\"pageable\":{\"pageSize\":24,\"sort\":[]}}";

    BasicOperations bo2 = new BasicOperations();
    bo2.reqHeaders = new BasicHeader[] { new BasicHeader("Authorization", authorizationBearer) };

    try {
      respStr = bo2.basicPost(payload, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking/search");
    } catch (Exception ex) {
      return null;
    }

    ResponseBooking response = gson.fromJson(respStr, ResponseBooking.class);

    return response;

  }

  public static String getTimeFormat(String timeFormat) {

    SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm");

    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Date date = null;
    try {
      date = oldFormat.parse(timeFormat);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    return newFormat.format(cal.getTime());

  }

  public static Booking tableBooked(ResponseBooking response, IntentRequest intentRequest) {

    long flexTime = 1800000;

    Slot queryTable = intentRequest.getIntent().getSlots().get("queryTable");
    Date date = new Date();
    long timeNow = date.getTime();

    for (com.entity.booking.Content c : response.content) {

      String BookingTimeLongAsString = c.booking.bookingDate.substring(0, 10) + "";

      if (Integer.parseInt(c.booking.tableId) == Integer.parseInt(queryTable.getValue())
          && Math.abs((Long.parseLong(BookingTimeLongAsString) * 1000) - timeNow) <= flexTime) {

        HelpClass.req = new RequestOrder();
        HelpClass.req.booking.bookingToken = c.booking.bookingToken;
        HelpClass.req.booking.name = c.booking.name;
        HelpClass.req.booking.assistants = c.booking.assistants;
        HelpClass.req.booking.email = c.booking.email;

        return c.booking;

      }

    }

    return null;
  }

  public static String getDateFormat(String dateFormat) {

    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");

    SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Date date = null;
    try {
      date = oldFormat.parse(dateFormat);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);

    return newFormat.format(cal.getTime());

  }

  public static String getFormatDateTimeAndCalculate(String date_time) {

    SimpleDateFormat olfFormat = new SimpleDateFormat("yyyy-M-dd HH:mm");

    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    Date date = null;
    try {
      date = olfFormat.parse(date_time);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(Calendar.HOUR_OF_DAY, -2);

    return newFormat.format(cal.getTime());

  }

  public static boolean compareCurrentTimeServeTime(String serveTime, String currentTime) {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    Date serTime = null;
    Date curTime = null;

    try {
      curTime = sdf.parse(currentTime);
      serTime = sdf.parse(serveTime);
    } catch (ParseException e) {
      return false;
    }

    Calendar calSerTime = Calendar.getInstance();
    Calendar calCurTime = Calendar.getInstance();
    calSerTime.setTime(serTime);
    calCurTime.setTime(curTime);

    calCurTime.add(Calendar.MINUTE, 29);

    if (!calSerTime.after(calCurTime))
      return false;

    return true;

  }

  public static String getExtrasName(String dishID) {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();
    String resStr = "";

    try {
      resStr = bo.basicGET(BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/" + dishID + "/");
    } catch (Exception ex) {
      String speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
      return speechText;
    }

    OrderLines response = gson.fromJson(resStr, OrderLines.class);

    String extraStr = "Zu diesem Gericht können Sie ";

    extras = response.extras;

    for (int i = 0; i < extras.size(); i++) {

      if (i < response.extras.size() - 1)
        extraStr += response.extras.get(i).name + " für "
            + response.extras.get(i).price.replaceAll("0", "").replace(".", " Euro") + " und oder ";
      else
        extraStr += response.extras.get(i).name + " für "
            + response.extras.get(i).price.replaceAll("0", "").replace(".", " Euro") + " wählen.";

    }

    return extraStr;

  }

  public static ArrayList<String> getExtrasNameArray() {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();
    String resStr = "";

    String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      return null;
    }

    ResponseMenuDishes response = gson.fromJson(resStr, ResponseMenuDishes.class);

    ArrayList<String> extraArray = new ArrayList<>();

    for (Content c : response.content) {
      for (Extras e : c.extras) {

        boolean found = false;
        for (String s : extraArray) {

          if (e.name.equals(s))
            found = true;

        }

        if (!found)
          extraArray.add(e.name);

      }
    }

    return extraArray;

  }

  public static String sendOrder() {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();

    String payload = gson.toJson(req);

    String resp = "";
    try {
      resp = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/ordermanagement/v1/order");
    } catch (Exception ex) {
      String speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt "
          + payload;
      return speechText;
    }

    return "Vielen Dank. Ihre Bestellung wurde aufgenommen";

  }

  public static String getDishId(String dishName) {

    BasicOperations bo = new BasicOperations();
    Gson gson = new Gson();
    String resStr = "";

    String payload = "{\"categories\":[],\"searchBy\":\"\",\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[{\"property\":\"price\",\"direction\":\"DESC\"}]},\"maxPrice\":null,\"minLikes\":null}";

    try {
      resStr = bo.basicPost(payload, BASE_URL + "/mythaistar/services/rest/dishmanagement/v1/dish/search");
    } catch (Exception ex) {
      return "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
    }

    ResponseDescriptionDishes response = gson.fromJson(resStr, ResponseDescriptionDishes.class);

    for (int i = 0; i < response.content.length; i++) {
      if (response.content[i].dish.name.toLowerCase().equals(dishName)) {
        return response.content[i].dish.id;
      }
    }
    return null;
  }

}
