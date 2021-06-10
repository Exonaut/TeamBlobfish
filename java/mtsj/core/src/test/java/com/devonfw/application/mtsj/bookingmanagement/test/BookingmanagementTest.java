package com.devonfw.application.mtsj.bookingmanagement.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;

/**
 * Test for {@link Bookingmanagement}
 *
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = SpringBootApp.class)
class BookingmanagementTest extends ApplicationComponentTest {

  @MockBean
  private BookingRepository bookingDao;

  @InjectMocks
  private Bookingmanagement bookingManagement;

  @Mocks
  private BookingCto bookingCto;

  @Override
  public void doSetUp() {

    super.setUp();

    // this.bookingManagement = new Bookingmanagement();
    BookingEto booking = new BookingEto();
    booking.setBookingToken("CB_20170509_123502555Z");

    // booking.setCanceled(false);
    // booking.setAssistants(3);

  }

  @Test
  public void checkBookingIdExist() throws Exception {

    Long bookingId = ((long) 0);
    BookingCto cto = this.bookingManagement.findBooking(bookingId);
    assertThat(cto).isNotNull();
  }

  @Test
  public void checkBookingIdDoesNotExist() throws Exception {

    Long bookingId = null;
    BookingCto cto = this.bookingManagement.findBooking(bookingId);
    assertThat(cto).isNull();
  }

  /**
   * Tests that a booking is created
   */
  @Test
  public void saveBooking() {

    BookingEto booking = this.bookingManagement.saveBooking(this.bookingCto);
    assertThat(booking).isNotNull();
  }

}
