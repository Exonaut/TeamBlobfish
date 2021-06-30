package com.devonfw.application.mtsj.bookingmanagement.common.api.to;

import com.devonfw.application.mtsj.bookingmanagement.common.api.Table;
import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * Entity transport object of Table
 */
public class TableEto extends AbstractEto implements Table {

  private static final long serialVersionUID = 1L;

  private int seatsNumber;

  @Override
  public int getSeatsNumber() {

    return this.seatsNumber;
  }

  @Override
  public void setSeatsNumber(int seatsNumber) {

    this.seatsNumber = seatsNumber;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (this.seatsNumber < 0 ? 0 : ((Integer) this.seatsNumber).hashCode());
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
    TableEto other = (TableEto) obj;
    if (this.seatsNumber < 0) {
      if (other.seatsNumber > 0) {
        return false;
      }
    } else if (this.seatsNumber != (other.seatsNumber)) {
      return false;
    }
    return true;
  }

}
