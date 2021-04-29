package com.devonfw.application.mtsj.ordermanagement.common.api.to;

import java.util.List;

import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.InvitedGuestEto;
import com.devonfw.module.basic.common.api.to.AbstractCto;

/**
 * Composite transport object of Order
 */
public class OrderCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private OrderEto order;

  private BookingEto booking;

  private InvitedGuestEto invitedGuest;

  private List<OrderLineCto> orderLines;

  private BookingEto host;

  private Long orderstatus;

  private Long paymentstatus;

  public Long getOrderStatus() {

    return this.orderstatus;
  }

  public void setOrderStatus(Long orderstatus) {

    this.orderstatus = orderstatus;
  }

  public OrderEto getOrder() {

    return this.order;
  }

  public void setOrder(OrderEto order) {

    this.order = order;
  }

  public BookingEto getBooking() {

    return this.booking;
  }

  public void setBooking(BookingEto booking) {

    this.booking = booking;
  }

  public InvitedGuestEto getInvitedGuest() {

    return this.invitedGuest;
  }

  public void setInvitedGuest(InvitedGuestEto invitedGuest) {

    this.invitedGuest = invitedGuest;
  }

  public List<OrderLineCto> getOrderLines() {

    return this.orderLines;
  }

  public void setOrderLines(List<OrderLineCto> orderLines) {

    this.orderLines = orderLines;
  }

  public BookingEto getHost() {

    return this.host;
  }

  public void setHost(BookingEto host) {

    this.host = host;
  }

  /**
   * @return paymentstatus
   */
  public Long getPaymentStatus() {

    return this.paymentstatus;
  }

  /**
   * @param paymentstatus new value of {@link #getpaymentstatus}.
   */
  public void setPaymentStatus(Long paymentstatus) {

    this.paymentstatus = paymentstatus;
  }

}
