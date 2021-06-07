package com.devonfw.application.mtsj.bookingmanagement.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.BookingEntity;
import com.devonfw.application.mtsj.bookingmanagement.dataaccess.api.repo.BookingRepository;

/**
 * TODO prett This type ... Null PointerException
 *
 */
class BookingRepositoryTest {

  @Autowired
  private BookingRepository bookingRepository;

  @Test
  void findBookingByToken() {

    // give
    String bookingToken = new String("CB_20170509_123502555Z");

    // then
    BookingEntity bookingEntity = this.bookingRepository.findBookingByToken(bookingToken);

    // when

  }

}
