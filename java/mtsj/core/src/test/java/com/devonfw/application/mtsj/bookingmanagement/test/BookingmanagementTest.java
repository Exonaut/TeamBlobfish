package com.devonfw.application.mtsj.bookingmanagement.test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingCto;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;
import com.devonfw.application.mtsj.bookingmanagement.logic.api.Bookingmanagement;
import com.devonfw.application.mtsj.bookingmanagement.logic.impl.BookingmanagementImpl;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.general.common.base.AbstractBeanMapperSupport;

/**
 * Test for {@link Bookingmanagement}
 *
 */
// @ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = SpringBootApp.class)
class BookingmanagementTest extends ApplicationComponentTest {

  private Bookingmanagement bookingManagement;

  private BookingCto bookingCto;

  private AbstractBeanMapperSupport bean;

  private BookingRepository bookingDao = Mockito.mock(BookingRepository.class);

  @Override
  @BeforeEach
  public void doSetUp() {

    // super.setUp();
    // MockitoAnnotations.initMocks(this);
    this.bookingManagement = new BookingmanagementImpl();

    BookingEto booking = new BookingEto();
    // testing user0
    booking.setBookingToken("CB_20170509_123502555Z");
    this.bookingCto = new BookingCto();
    this.bookingCto.setBooking(booking);
  }

  @Test
  public void findBookingByGivenId() {

    // give
    Long bookingId = (long) 0;

    // when
    // BookingCto cto = this.bookingManagement.findBooking(bookingId);
    BookingEntity entity = this.bookingDao.find(bookingId);
    when(this.bookingDao.save((entity))).thenReturn(entity);
    // when(bookingRepository.save(eq(booking()))).thenReturn(booking());
    // BookingCto cto = new BookingCto();

    // then
    assertThat(entity).isNotNull();
    assertThat(entity.getId()).isEqualTo(bookingId);
    verify(this.bookingDao).find(bookingId);
  }

  // @Test
  // public void checkBookingIdExist() throws Exception {
  //
  // Long bookingId = ((long) 0);
  // BookingCto cto = this.bookingManagement.findBooking(bookingId);
  // assertThat(cto).isNotNull();
  // }
  //
  // @Test
  // public void checkBookingIdDoesNotExist() throws Exception {
  //
  // Long bookingId = null;
  // BookingCto cto = this.bookingManagement.findBooking(bookingId);
  // assertThat(cto).isNull();
  // }
  //
  // /**
  // * Tests that a booking is created
  // */
  // @Test
  // public void saveBooking() {
  //
  // BookingEto booking = this.bookingManagement.saveBooking(this.bookingCto);
  // assertThat(booking).isNotNull();
  // }

}
