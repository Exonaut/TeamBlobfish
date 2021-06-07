package com.devonfw.application.mtsj.bookingmanagement.test;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Test for {@link Bookingmanagement}
 *
 */
class BookingmanagementImplTest extends ApplicationComponentTest {

  @Inject
  private Bookingmanagement bookingManagement;

  private BookingCto bookingCto;

  @Override
  public void doSetUp() {

    super.setUp();

  }

  @Test
  void test() {

    fail("Not yet implemented");
  }

}
