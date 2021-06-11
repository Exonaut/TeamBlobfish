package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderEntity;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo.OrderRepository;

/**
 * Tests for {@link OrderRepository}
 *
 */
@DataJpaTest
public class OrderRepositoryTest {

  @Autowired
  private OrderRepository repo;

  /**
   * @throws java.lang.Exception
   */
  @BeforeEach
  void setUp() throws Exception {

  }

  /**
   * @throws java.lang.Exception
   */
  @AfterEach
  void tearDown() throws Exception {

  }

  @Test
  void checkIfOrdersExistsByValidBookingId() {

    // given
    Long idBooking = (long) 0;
    BookingEto booking = new BookingEto();
    booking.setId(idBooking);

    // when
    List<OrderEntity> orders = this.repo.findOrders(idBooking);
    String c = null;

    // then
    assertThat(orders).size().isGreaterThan(0);
  }

}
