package com.devonfw.application.mtsj.bookingmanagement.test;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Test for {@link Bookingmanagement}
 *
 */
class BookingmanagementTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingManagement;

  private BookingCto bookingCto;

  @Override
  public void doSetUp() {

    super.setUp();

    BookingEto booking = new BookingEto();
    booking.setBookingToken("CB_20170509_123502555Z");

    booking.setCanceled(false);
    booking.setAssistants(3);

  }

  @Test
  void test() {

    fail("Not yet implemented");
  }

}
