package com.devonfw.application.mtsj.ordermanagement.common.api;

import java.time.Instant;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

public interface Order extends ApplicationEntity {

  public Long getBookingId();

  public void setBookingId(Long bookingId);

  public Long getInvitedGuestId();

  public void setInvitedGuestId(Long invitedGuestId);

  public Long getHostId();

  public void setHostId(Long hostId);

  public Long getOrderStatus();

  public void setOrderStatus(Long orderstatus);

  public Long getPaymentStatus();

  public void setPaymentStatus(Long paymentstatus);

  public Instant getServingTime();

  public void setServingTime(Instant servingtime);

}
