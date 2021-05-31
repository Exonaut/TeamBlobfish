package com.devonfw.application.mtsj.ordermanagement.common.api;

import java.time.Instant;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

public interface OrderLine extends ApplicationEntity {

  public Long getOrderId();

  public void setOrderId(Long orderId);

  public Long getDishId();

  public void setDishId(Long dishId);

  public Integer getAmount();

  public void setAmount(Integer amount);

  public String getComment();

  public void setComment(String comment);

  public Instant getServingTime();

  public void setServingTime(Instant servingtime);

}
