package com.tools;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

public class CookieMonster {

  public String CSRF = "";

  public String Session_ID = "";

  public String User_ID = "";

  public String MID = "";

  public CookieStore httpCookieStore = new BasicCookieStore();

  public void convertCookies() {

  }

  public String getCookieString() {

    updateCookies();

    String cookie_string = "";
    for (Cookie cookie : this.httpCookieStore.getCookies()) {
      cookie_string = cookie_string + cookie.getName() + "=" + cookie.getValue() + ";";
    }
    return cookie_string;

  }

  public void displayCookies() {

    for (Cookie cookie : this.httpCookieStore.getCookies()) {
      System.out.println(cookie);

    }
  }

  public void updateCookies() {

    for (Cookie cookie : this.httpCookieStore.getCookies()) {
      if (cookie.getName().equals("csrftoken")) {
        this.CSRF = cookie.getValue();
      }
      if (cookie.getName().equals("sessionid")) {
        this.Session_ID = cookie.getValue();
      }
      if (cookie.getName().equals("ds_user_id")) {
        this.User_ID = cookie.getValue();
      }
      if (cookie.getName().equals("mid")) {
        this.MID = cookie.getValue();
      }
    }
  }

}
