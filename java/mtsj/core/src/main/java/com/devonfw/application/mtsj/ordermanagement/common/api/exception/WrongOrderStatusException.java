package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * This exception is thrown if {@link com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto orderstatus}
 * is wrong. Order status could be between 0 and 6.
 *
 */
public class WrongOrderStatusException extends NlsRuntimeException {

  public WrongOrderStatusException() {

    super("Order status is wrong.");
  }
}
