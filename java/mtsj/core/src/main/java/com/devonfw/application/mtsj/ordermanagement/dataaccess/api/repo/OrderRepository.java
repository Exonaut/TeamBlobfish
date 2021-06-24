package com.devonfw.application.mtsj.ordermanagement.dataaccess.api.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devonfw.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import com.devonfw.application.mtsj.ordermanagement.common.api.to.OrderSearchCriteriaTo;
import com.devonfw.application.mtsj.ordermanagement.dataaccess.api.OrderEntity;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * {@link DefaultRepository} for {@link OrderEntity}.
 */
public interface OrderRepository extends DefaultRepository<OrderEntity> {

  /**
   * @param idBooking
   * @return the list {@link OrderEntity} objects that matched the search.
   */
  @Query("SELECT orders FROM OrderEntity orders" //
      + " WHERE orders.booking.id = :idBooking")
  List<OrderEntity> findOrders(@Param("idBooking") Long idBooking);

  /**
   * @param idInvitedGuest
   * @return the list {@link OrderEntity} objects that matched the search.
   */
  @Query("SELECT orders FROM OrderEntity orders" //
      + " WHERE orders.invitedGuest.id = :idInvitedGuest")
  List<OrderEntity> findOrdersByInvitedGuest(@Param("idInvitedGuest") Long idInvitedGuest);

  /**
   * @param bookingToken
   * @return the {@link OrderEntity} objects that matched the search.
   */
  @Query("SELECT orders FROM OrderEntity orders" //
      + " WHERE orders.booking.bookingToken = :bookingToken")
  List<OrderEntity> findOrdersByBookingToken(@Param("bookingToken") String bookingToken);

  /**
   * @param criteria the {@link OrderSearchCriteriaTo} with the criteria to search.
   * @return the {@link Page} of the {@link OrderEntity} objects that matched the search.
   */
  default Page<OrderEntity> findOrders(OrderSearchCriteriaTo criteria) {

    OrderEntity alias = newDslAlias();
    JPAQuery<OrderEntity> query = newDslQuery(alias);

    Long[] orderstatus = criteria.getOrderstatus();
    Long[] paymentstatus = criteria.getPaymentstatus();
    if (alias.getOrderStatus() != null && orderstatus != null) {
      query.where(
          Alias.$(alias.getOrderStatus()).in(orderstatus).and(Alias.$(alias.getPaymentStatus()).in(paymentstatus)));
    }

    Long booking = criteria.getBookingId();
    if (booking != null && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getId()).eq(booking));
    }

    Long invitedGuest = criteria.getInvitedGuestId();
    if (invitedGuest != null && alias.getInvitedGuest() != null) {
      query.where(Alias.$(alias.getInvitedGuest().getId()).eq(invitedGuest));
    }

    String hostToken = criteria.getHostToken();
    if (hostToken != null && alias.getHost() != null) {
      query.where(Alias.$(alias.getBooking().getBookingToken()).eq(hostToken));
    }

    String email = criteria.getEmail();
    if ((email != null) && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getEmail()).toLowerCase().contains(email.toLowerCase()));
    }

    String bookingToken = criteria.getBookingToken();
    if ((bookingToken != null) && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getBookingToken()).toLowerCase().eq(bookingToken.toLowerCase()));
    }

    String name = criteria.getName();
    if ((name != null) && alias.getBooking() != null) {
      query.where(Alias.$(alias.getBooking().getName()).toLowerCase().contains(name.toLowerCase()));
    }

    Long table = criteria.getTable();
    Long t = alias.getBooking().getTable().getId();
    if ((table != null) && t != null) {
      query.where(Alias.$(t).eq(table));
    }

    Integer bookingType = criteria.getBookingType();
    if (bookingType != null) {
      BookingType type = BookingType.values()[bookingType];
      if ((type != null) && alias.getBooking().getBookingType() != null) {
        query.where(Alias.$(alias.getBooking().getBookingType()).eq(type));
      }
    }

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, true);
  }

}
