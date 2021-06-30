package com.devonfw.application.mtsj.ordermanagement.common.api.exception;

import net.sf.mmm.util.exception.api.NlsRuntimeException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * New exception class in case if an order was not found
 *
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No order found")
public class NoOrderException extends NlsRuntimeException {

  public NoOrderException() {

    super("There is no order");
  }

}
