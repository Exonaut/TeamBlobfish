package com.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import com.alexa.myThaiStar.MyThaiStarStreamHandler;
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
import com.tools.exceptions.Different;
import com.tools.exceptions.NotFound;

public class BasicOperations {

  public static String BASE_URL = MyThaiStarStreamHandler.BASE_URL;

  public static RequestOrder req;

  public static ArrayList<Extras> extras;

  public static long bookingDateTimeMilliseconds;

  public static String dishID;

  final int TIME_OUT = 10000;

  HttpClient http = null;

  org.apache.http.client.config.RequestConfig RequestConfig;

  public Header[] reqHeaders;

  public Header[] resheaders;

  public BasicOperations() {

    // refreshHeader();
    buildHttpClient();

  }

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

  public static Booking tableBooked(ResponseBooking response, String queryTable) {

    long flexTime = 1800000;

    Date date = new Date();
    long timeNow = date.getTime();

    for (com.entity.booking.Content c : response.content) {

      String BookingTimeLongAsString = c.booking.bookingDate.substring(0, 10) + "";

      if (Integer.parseInt(c.booking.tableId) == Integer.parseInt(queryTable)
          && Math.abs((Long.parseLong(BookingTimeLongAsString) * 1000) - timeNow) <= flexTime) {

        String tmpBookingTimeAsString = c.booking.bookingDate.substring(0, 10) + "";

        BasicOperations.req = new RequestOrder();
        BasicOperations.req.booking.bookingToken = c.booking.bookingToken;
        BasicOperations.req.booking.name = c.booking.name;
        BasicOperations.req.booking.assistants = c.booking.assistants;
        BasicOperations.req.booking.email = c.booking.email;
        BasicOperations.bookingDateTimeMilliseconds = (Long.parseLong(tmpBookingTimeAsString) * 1000) + 7200000;

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

  public String basicGET(String url) throws Different, NotFound, IOException {

    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(this.RequestConfig);
    httpGet.setHeaders(this.reqHeaders);

    System.out.println(url);
    HttpResponse httpResponse = this.http.execute(httpGet);

    String return_string = InputToString(httpResponse.getEntity().getContent());

    // cookieMonster.displayCookies();
    httpResponse.getEntity().getContent().close();

    if (httpResponse.getStatusLine().getStatusCode() == 404) {
      httpGet.completed();
      httpGet.releaseConnection();
      System.out.println(url);
      throw new NotFound();
    } else if (httpResponse.getStatusLine().getStatusCode() != 200) {
      httpGet.completed();
      httpGet.releaseConnection();
      System.out.println(httpResponse.getStatusLine().getStatusCode());
      System.out.println(url);
      System.out.println(return_string);
      throw new Different();
    } else {
      httpGet.completed();
      httpGet.releaseConnection();
      return return_string;
    }

  }

  public void buildHttpClient() {

    HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
      @Override
      public boolean retryRequest(IOException e, int i, HttpContext httpContext) {

        if (i < 3) {
          return true;
        } else {
          return false;
        }
      }
    };

    HttpClientBuilder builder = HttpClientBuilder.create();

    builder.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
    builder.setRedirectStrategy(new LaxRedirectStrategy());
    builder.setRetryHandler(retryHandler);

    org.apache.http.client.config.RequestConfig.Builder requestBuilder = org.apache.http.client.config.RequestConfig
        .custom();

    requestBuilder.setConnectTimeout(this.TIME_OUT);
    requestBuilder.setConnectionRequestTimeout(this.TIME_OUT);
    requestBuilder.setSocketTimeout(60000);

    // requestBuilder.setProxy(ProxyLoader.loadProxy());
    this.RequestConfig = requestBuilder.build();

    this.http = null;
    this.http = builder.build();

  }

  public String basicPost(String json_payload, String url) throws IOException, NotFound, Different {

    HttpPost httpPost = new HttpPost(url);
    httpPost.setConfig(this.RequestConfig);
    httpPost.setHeaders(this.reqHeaders);

    StringEntity entity = new StringEntity(json_payload, ContentType.APPLICATION_JSON);
    httpPost.setEntity(entity);
    HttpResponse httpResponse = this.http.execute(httpPost);

    this.resheaders = httpResponse.getAllHeaders();

    String return_string = InputToString(httpResponse.getEntity().getContent());

    httpResponse.getEntity().getContent().close();

    if (httpResponse.getStatusLine().getStatusCode() == 404) {
      httpPost.completed();
      httpPost.releaseConnection();
      System.out.println(url);
      throw new NotFound();
    } else if (httpResponse.getStatusLine().getStatusCode() != 200) {
      httpPost.completed();
      httpPost.releaseConnection();

      System.out.println(httpResponse.getStatusLine().getStatusCode());
      System.out.println(url);
      throw new Different();

    } else {
      httpPost.completed();
      httpPost.releaseConnection();

    }

    return return_string;
  }

  public String getSpecificHeader(String specificHeader) {

    for (Header header : this.resheaders) {
      if (header.getName().equals(specificHeader))
        return header.getValue();
    }
    return "";
  }

  public String InputToString(InputStream in) throws IOException {

    try {
      StringBuilder stringBuilder = new StringBuilder();
      Reader reader = new InputStreamReader(in);
      int data = reader.read();

      while (data != -1) {
        stringBuilder.append((char) data);
        data = reader.read();
      }
      return stringBuilder.toString();
    } catch (IOException ex) {
      try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      throw ex;
    }
  }
}
