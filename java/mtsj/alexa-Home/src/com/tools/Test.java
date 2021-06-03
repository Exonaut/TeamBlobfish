package com.tools;

public class Test {

  public static long flexTime = 1800000;

  public static String BASE_URL = "https://c640d4f8e051.ngrok.io";

  public static void main(String[] args) {

    // RequestLogin req = new RequestLogin();
    // req.password = "waiter";
    // req.username = "waiter";
    // Gson gson = new Gson();
    // String payload = gson.toJson(req);
    // String speechText = "";
    // String respStr = "";
    // BasicOperations bo = new BasicOperations();
    //
    // try {
    // bo.basicPost(payload, BASE_URL + "/mythaistar/login");
    // } catch (Exception ex) {
    // speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
    // }
    //
    // String authorizationBearer = bo.getSpecificHeader("Authorization");
    // payload = "{\"pageable\":{\"pageSize\":8,\"pageNumber\":0,\"sort\":[]}}";
    //
    // BasicOperations bo2 = new BasicOperations();
    // bo2.reqHeaders = new BasicHeader[] { new BasicHeader("Authorization", authorizationBearer) };
    //
    // try {
    // respStr = bo2.basicPost(payload, BASE_URL + "/mythaistar/services/rest/bookingmanagement/v1/booking/search");
    // } catch (Exception ex) {
    // speechText = "Es tut mir leid, es ist ein Problem aufgetreten. Versuchen Sie es zu einem späteren Zeitpunkt";
    // }
    //
    // ResponseBooking response = gson.fromJson(respStr, ResponseBooking.class);
    // for (Content c : response.content) {
    //
    // if (c.booking.email.equals("tony2510@gmx.de")) {
    //
    // HelperOrderClass.req = new RequestOrder();
    // HelperOrderClass.req.booking.bookingToken = c.booking.bookingToken;
    // HelperOrderClass.req.booking.bookingDate = c.booking.bookingDate.replace(".000000000", "");
    //
    // }
    // }
    //
    // String bookingDateTime = HelperOrderClass.convertSecondsToDateTime(HelperOrderClass.req.booking.bookingDate);
    // String bookingDate = HelperOrderClass.getDateFormat(bookingDateTime);
    //
    // HelperOrderClass.req.order.serveTime = HelperOrderClass
    // .getFormatDateTimeAndCalculate(bookingDate + " " + bookingDateTime);
    //
    // HelperOrderClass.sendOrder();

    String currentTime = HelperOrderClass
        .getTimeFormat(HelperOrderClass.convertMillisecondsToDateTime(System.currentTimeMillis()));

    if (!HelperOrderClass.compareCurrentTimeServeTime("13:46", currentTime)) {

      System.out.println(HelperOrderClass.compareCurrentTimeServeTime("13:15", currentTime));

    }

  }

}
