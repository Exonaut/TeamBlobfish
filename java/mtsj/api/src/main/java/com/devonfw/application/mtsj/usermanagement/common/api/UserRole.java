package com.devonfw.application.mtsj.usermanagement.common.api;

import com.devonfw.application.mtsj.general.common.api.ApplicationEntity;

public interface UserRole extends ApplicationEntity {

  public String getName();

  public void setName(String name);

  // set primitive type
  public boolean getActive();

  // set primitive type
  public void setActive(boolean active);

}
