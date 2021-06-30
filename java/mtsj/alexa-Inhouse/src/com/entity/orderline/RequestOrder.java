package com.entity.orderline;

import java.util.ArrayList;

import com.entity.booking.Booking;
import com.entity.orders.Orders;

public class RequestOrder {

  public Booking booking = new Booking();

  public ArrayList<OrderLines> orderLines = new ArrayList<OrderLines>();

  public Orders order = new Orders();

}
