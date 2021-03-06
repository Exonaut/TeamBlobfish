package com.devonfw.application.mtsj.ordermanagement.common.api.to;

import java.time.Instant;

import com.devonfw.application.mtsj.ordermanagement.common.api.Order;
import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * Entity transport object of Order
 */
public class OrderEto extends AbstractEto implements Order {

  private static final long serialVersionUID = 1L;

  private Long bookingId;

  private Long invitedGuestId;

  private String bookingToken;

  private Long orderstatus;

  private Long paymentstatus;

  private Instant servetime;

  private String city;

  private String street;

  private String streetNr;

  @Override
  public String getStreetNr() {

    return this.streetNr;
  }

  @Override
  public void setStreetNr(String streetNr) {

    this.streetNr = streetNr;

  }

  @Override
  public void setCity(String city) {

    this.city = city;
  }

  @Override
  public String getCity() {

    return this.city;
  }

  @Override
  public void setStreet(String street) {

    this.street = street;

  }

  @Override
  public String getStreet() {

    return this.street;
  }

  @Override
  public void setServeTime(Instant servetime) {

    this.servetime = servetime;

  }

  @Override
  public Instant getServeTime() {

    return this.servetime;
  }

  /**
   * @return bookingToken
   */
  public String getBookingToken() {

    return this.bookingToken;
  }

  /**
   * @param bookingToken new value of {@link #getbookingToken}.
   */
  public void setBookingToken(String bookingToken) {

    this.bookingToken = bookingToken;
  }

  private Long hostId;

  @Override
  public Long getBookingId() {

    return this.bookingId;
  }

  @Override
  public void setBookingId(Long bookingId) {

    this.bookingId = bookingId;
  }

  @Override
  public Long getInvitedGuestId() {

    return this.invitedGuestId;
  }

  @Override
  public void setInvitedGuestId(Long invitedGuestId) {

    this.invitedGuestId = invitedGuestId;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = super.hashCode();

    result = prime * result + ((this.bookingId == null) ? 0 : this.bookingId.hashCode());

    result = prime * result + ((this.invitedGuestId == null) ? 0 : this.invitedGuestId.hashCode());

    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    // class check will be done by super type EntityTo!
    if (!super.equals(obj)) {
      return false;
    }
    OrderEto other = (OrderEto) obj;

    if (this.bookingId == null) {
      if (other.bookingId != null) {
        return false;
      }
    } else if (!this.bookingId.equals(other.bookingId)) {
      return false;
    }

    if (this.invitedGuestId == null) {
      if (other.invitedGuestId != null) {
        return false;
      }
    } else if (!this.invitedGuestId.equals(other.invitedGuestId)) {
      return false;
    }

    return true;
  }

  @Override
  public Long getHostId() {

    return this.hostId;
  }

  @Override
  public void setHostId(Long hostId) {

    this.hostId = hostId;
  }

  @Override
  public Long getOrderStatus() {

    return this.orderstatus;
  }

  @Override
  public void setOrderStatus(Long status) {

    this.orderstatus = status;
  }

  @Override
  public Long getPaymentStatus() {

    return this.paymentstatus;
  }

  @Override
  public void setPaymentStatus(Long payment) {

    this.paymentstatus = payment;
  }

}
