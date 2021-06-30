package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

/**
 * This exception is thrown if This exception is thrown if
 * {@link com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto paymentstatus} is wrong. Payment status
 * could be between 0 and 3.
 *
 */
public class WrongPaymentStatusException extends NlsRuntimeException {

  public WrongPaymentStatusException() {

    super("Payment status is wrong");
  }
}
