package com.devonfw.application.mtsj.ordermanagement.dataaccess.api;

import java.time.Instant;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.InvitedGuestEntity;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.ordermanagement.common.api.Order;

/**
 * The {@link com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity persistent entity} for
 * {@link Order}.
 */
@Entity
@Table(name = "Orders")
public class OrderEntity extends ApplicationPersistenceEntity implements Order {

  private static final long serialVersionUID = 1L;

  private BookingEntity booking;

  private InvitedGuestEntity invitedGuest;

  private BookingEntity host;

  private List<OrderLineEntity> orderLines;

  private Long orderstatus;

  private Long paymentstatus;

  private Instant serveTime;

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

    this.serveTime = servetime;

  }

  @Override
  public Instant getServeTime() {

    return this.serveTime;
  }

  /**
   * @return booking
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idBooking")
  public BookingEntity getBooking() {

    return this.booking;
  }

  /**
   * @param booking new value of {@link #getbooking}.
   */
  public void setBooking(BookingEntity booking) {

    this.booking = booking;
  }

  /**
   * @return invitedGuest
   */
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idInvitedGuest")
  public InvitedGuestEntity getInvitedGuest() {

    return this.invitedGuest;
  }

  /**
   * @param invitedGuest new value of {@link #getinvitedGuest}.
   */
  public void setInvitedGuest(InvitedGuestEntity invitedGuest) {

    this.invitedGuest = invitedGuest;
  }

  /**
   * @return orderLines
   */
  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
  public List<OrderLineEntity> getOrderLines() {

    return this.orderLines;
  }

  /**
   * @param orderLines new value of {@link #getorderLines}.
   */
  public void setOrderLines(List<OrderLineEntity> orderLines) {

    this.orderLines = orderLines;
  }

  @Override
  @Transient
  public Long getBookingId() {

    if (this.booking == null) {
      return null;
    }
    return this.booking.getId();
  }

  @Override
  public void setBookingId(Long bookingId) {

    if (bookingId == null) {
      this.booking = null;
    } else {
      BookingEntity bookingEntity = new BookingEntity();
      bookingEntity.setId(bookingId);
      this.booking = bookingEntity;
    }
  }

  @Override
  @Transient
  public Long getInvitedGuestId() {

    if (this.invitedGuest == null) {
      return null;
    }
    return this.invitedGuest.getId();
  }

  @Override
  public void setInvitedGuestId(Long invitedGuestId) {

    if (invitedGuestId == null) {
      this.invitedGuest = null;
    } else {
      InvitedGuestEntity invitedGuestEntity = new InvitedGuestEntity();
      invitedGuestEntity.setId(invitedGuestId);
      this.invitedGuest = invitedGuestEntity;
    }
  }

  /**
   * @return host
   */
  @OneToOne
  @JoinColumn(name = "idHost")
  public BookingEntity getHost() {

    return this.host;
  }

  /**
   * @param host new value of {@link #gethost}.
   */
  public void setHost(BookingEntity host) {

    this.host = host;
  }

  @Override
  @Transient
  public Long getHostId() {

    if (this.host == null) {
      return null;
    }
    return this.host.getId();
  }

  @Override
  public void setHostId(Long hostId) {

    if (hostId == null) {
      this.host = null;
    } else {
      BookingEntity bookingEntity = new BookingEntity();
      bookingEntity.setId(hostId);
      this.host = bookingEntity;
    }
  }

  @Override
  public Long getOrderStatus() {

    return this.orderstatus;
  }

  @Override
  public void setOrderStatus(Long status) {

    if (status != null)
      this.orderstatus = status;
    else
      this.orderstatus = (long) 0;

  }

  @Override
  public Long getPaymentStatus() {

    return this.paymentstatus;
  }

  @Override
  public void setPaymentStatus(Long payment) {

    if (payment != null) {
      this.paymentstatus = payment;
    } else {
      this.paymentstatus = (long) 0;
    }
  }

}
