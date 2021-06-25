package com.devonfw.application.mtsj.ordermanagement.logic.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.bookingmanagement.common.api.to.BookingEto;
import com.devonfw.application.mtsj.dishmanagement.common.api.to.DishEto;
import com.devonfw.application.mtsj.dishmanagement.dataaccess.api.IngredientEntity;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoBookingException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.NoInviteException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.OrderAlreadyExistException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.WrongOrderStatusException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.WrongPaymentStatusException;
import com.devonfw.application.mtsj.ordermanagement.common.api.exception.WrongTokenException;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderEto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineCto;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderLineEto;
import com.devonfw.application.mtsj.ordermanagement.logic.api.Ordermanagement;

/**
 * Test for {@link Ordermanagement}
 *
 */

/**
 * TRY ROLLBACKEXCEPTION WEG! TODO prett This type ...
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class OrdermanagementTest extends ApplicationComponentTest {

  @Inject
  private Ordermanagement orderManagement;

  OrderCto orderCto;

  /**
   * Creation of needed objects
   */
  @Override
  public void doSetUp() {

    super.doSetUp();

    // extra ingredients
    IngredientEntity i1 = new IngredientEntity();
    i1.setId(0L);
    IngredientEntity i2 = new IngredientEntity();
    i2.setId(1L);
    List<IngredientEntity> extras = new ArrayList<>();
    extras.add(i1);
    extras.add(i2);

    // Dish
    DishEto dishEto = new DishEto();
    dishEto.setId(5L);

    // OrderLine Eto 1
    OrderLineEto olEto1 = new OrderLineEto();
    olEto1.setAmount(3);
    olEto1.setComment("This is a test order line");
    olEto1.setDishId(dishEto.getId());

    // OrderLine Eto 2
    OrderLineEto olEto2 = new OrderLineEto();
    olEto2.setAmount(1);
    olEto2.setComment("This is another order line");
    olEto2.setDishId(dishEto.getId());

    // order line 1
    OrderLineCto ol1 = new OrderLineCto();
    ol1.setDish(dishEto);
    ol1.setOrderLine(olEto1);

    // order line 2
    OrderLineCto ol2 = new OrderLineCto();
    ol2.setDish(dishEto);
    ol1.setOrderLine(olEto2);

    // order
    List<OrderLineCto> lines = new ArrayList<>();
    lines.add(ol1);
    // lines.add(ol2);

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("CB_20170510_123502595Z");
    this.orderCto = new OrderCto();
    this.orderCto.setBooking(bookingEto);
    this.orderCto.setOrderLines(lines);

  }

  /**
   * Tests if an order is created
   */
  @Test
  public void orderAnOrder() {

    OrderEto createdOrder = this.orderManagement.saveOrder(this.orderCto);
    assertThat(createdOrder).isNotNull();

  }

  /**
   * Tests that an order with a wrong token is not created
   */
  @Test
  public void orderAnOrderWithWrongToken() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("wrongToken");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      WrongTokenException wte = new WrongTokenException();
      assertThat(e.getClass()).isEqualTo(wte.getClass());
    }
  }

  /**
   * Tests that an order with a empty token is not created
   */
  @Test
  public void orderAnOrderWithEmptyToken() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      WrongTokenException wte = new WrongTokenException();
      assertThat(e.getClass()).isEqualTo(wte.getClass());
    }
  }

  /**
   * Tests that an order that is already created, is not created again
   */
  @Test
  public void orderAnOrderAlreadyCreated() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("CB_20170509_123502555Z");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      OrderAlreadyExistException oae = new OrderAlreadyExistException();
      assertThat(e.getClass()).isEqualTo(oae.getClass());
    }
  }

  /**
   * Tests that an order with a booking token that does not exist is not created
   */
  @Test
  public void orderAnOrderBookingNotExist() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("CB_Not_Existing_Token");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      NoBookingException nb = new NoBookingException();
      assertThat(e.getClass()).isEqualTo(nb.getClass());
    }
  }

  /**
   * Tests that an order with a guest token that does not exist is not created
   */
  @Test
  public void orderAnOrderInviteNotExist() {

    BookingEto bookingEto = new BookingEto();
    bookingEto.setBookingToken("GB_Not_Existing_Token");
    this.orderCto.setBooking(bookingEto);
    try {
      this.orderManagement.saveOrder(this.orderCto);
    } catch (Exception e) {
      NoInviteException ni = new NoInviteException();
      assertThat(e.getClass()).isEqualTo(ni.getClass());
    }
  }

  /**
   * Tests to find orders by booking token
   */
  @Test
  public void findOrdersByToken() {

    String bookingToken = this.orderCto.getBooking().getBookingToken();
    List<OrderCto> orders = this.orderManagement.findOrdersByBookingToken(bookingToken);
    assertThat(orders).isNotNull();
  }

  /**
   * Tests to find an order by booking id
   */
  @Test
  public void findOrdersByGivenBookingId() {

    Long idBooking = (long) 0;
    OrderCto foundOrder = this.orderManagement.findOrder(idBooking);
    assertThat(foundOrder).isNotNull();
  }

  /**
   * Tests to edit new payment status of order
   */
  @Test
  public void setNewPaymentStatusTest() {

    Long orderId = (long) 0;
    Long paymentStatus = (long) 1;

    try {
      OrderEto orderEntity = this.orderManagement.setNewPaymentStatus(orderId, paymentStatus);
      assertThat(orderEntity.getPaymentStatus()).isEqualTo(paymentStatus);
    } catch (Exception e) {
      WrongPaymentStatusException we = new WrongPaymentStatusException();
      assertThat(e.getClass()).isEqualTo(we.getClass());
    }
  }

  /**
   * Tests to edit new payment status of order that is invalid
   */
  @Test
  public void setInvalidPaymentStatusTest1() {

    Long orderId = (long) 0;
    Long paymentStatus = (long) 3;

    try {
      OrderEto orderEntity = this.orderManagement.setNewPaymentStatus(orderId, paymentStatus);
      assertThat(orderEntity.getPaymentStatus()).isEqualTo(paymentStatus);
    } catch (Exception e) {
      WrongPaymentStatusException we = new WrongPaymentStatusException();
      assertThat(e.getClass()).isEqualTo(we.getClass());
    }
  }

  /**
   * Tests to edit new payment status of order that is invalid
   */
  @Test
  public void setInvalidPaymentStatusTest2() {

    Long orderId = (long) 0;
    Long paymentStatus = (long) -1;

    try {
      OrderEto orderEntity = this.orderManagement.setNewPaymentStatus(orderId, paymentStatus);
      assertThat(orderEntity.getPaymentStatus()).isEqualTo(paymentStatus);
    } catch (Exception e) {
      WrongPaymentStatusException we = new WrongPaymentStatusException();
      assertThat(e.getClass()).isEqualTo(we.getClass());
    }
  }

  /**
   * Tests to edit new order status of order
   */
  @Test
  public void setNewOrderStatusTest() {

    Long orderId = (long) 0;
    Long orderStatus = (long) 3;

    try {
      OrderEto orderEntity = this.orderManagement.setNewOrderStatus(orderId, orderStatus);
      assertThat(orderEntity.getOrderStatus()).isEqualTo(orderStatus);
    } catch (Exception e) {
      WrongOrderStatusException wo = new WrongOrderStatusException();
      assertThat(e.getClass()).isEqualTo(wo.getClass());
    }
  }

  /**
   * Tests to edit new order status of order
   */
  @Test
  public void setInvalidOrderStatusTest1() {

    Long orderId = (long) 0;
    Long orderStatus = (long) -1;

    try {
      OrderEto orderEntity = this.orderManagement.setNewOrderStatus(orderId, orderStatus);
      assertThat(orderEntity.getOrderStatus()).isEqualTo(orderStatus);
    } catch (Exception e) {
      WrongOrderStatusException wo = new WrongOrderStatusException();
      assertThat(e.getClass()).isEqualTo(wo.getClass());
    }
  }

  /**
   * Tests to edit new order status of order
   */
  @Test
  public void setInvalidOrderStatusTest2() {

    Long orderId = (long) 0;
    Long orderStatus = (long) 7;

    try {
      OrderEto orderEntity = this.orderManagement.setNewOrderStatus(orderId, orderStatus);
      assertThat(orderEntity.getOrderStatus()).isEqualTo(orderStatus);
    } catch (Exception e) {
      WrongOrderStatusException wo = new WrongOrderStatusException();
      assertThat(e.getClass()).isEqualTo(wo.getClass());
    }
  }
}
