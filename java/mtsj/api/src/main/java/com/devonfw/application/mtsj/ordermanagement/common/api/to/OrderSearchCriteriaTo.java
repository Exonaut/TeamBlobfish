package com.devonfw.application.mtsj.ordermanagement.common.api.to;

import com.devonfw.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;

/**
 * used to find {@link com.devonfw.application.mtsj.ordermanagement.common.api.Order}s.
 */
public class OrderSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Long bookingId;

  private Long invitedGuestId;

  private String hostToken;

  private Long hostId;

  private String email;

  private String bookingToken;

  private StringSearchConfigTo hostTokenOption;

  private StringSearchConfigTo emailOption;

  private StringSearchConfigTo bookingTokenOption;

  private Long[] orderstatus;

  private Long[] paymentstatus;

  private String name;

  private Long table;

  private Integer bookingType;

  /**
   * The constructor.
   */
  public OrderSearchCriteriaTo() {

    super();
  }

  public Integer getBookingType() {

    return this.bookingType;
  }

  public void setBookingType(Integer type) {

    this.bookingType = type;
  }

  public Long getTable() {

    return this.table;
  }

  public void setTable(Long table) {

    this.table = table;
  }

  public Long getBookingId() {

    return this.bookingId;
  }

  public void setBookingId(Long bookingId) {

    this.bookingId = bookingId;
  }

  public Long getInvitedGuestId() {

    return this.invitedGuestId;
  }

  public void setInvitedGuestId(Long invitedGuestId) {

    this.invitedGuestId = invitedGuestId;
  }

  public String getHostToken() {

    return this.hostToken;
  }

  public void setHostToken(String hostToken) {

    this.hostToken = hostToken;
  }

  public Long getHostId() {

    return this.hostId;
  }

  public void setHostId(Long hostId) {

    this.hostId = hostId;
  }

  /**
   * @return email
   */
  public String getEmail() {

    return this.email;
  }

  /**
   * @param email new value of {@link #getEmail}.
   */
  public void setEmail(String email) {

    this.email = email;
  }

  /**
   * @return bookingToken
   */
  public String getBookingToken() {

    return this.bookingToken;
  }

  /**
   * @param bookingToken new value of {@link #getBookingToken}.
   */
  public void setBookingToken(String bookingToken) {

    this.bookingToken = bookingToken;
  }

  /**
   * @return hostTokenOption
   */
  public StringSearchConfigTo getHostTokenOption() {

    return this.hostTokenOption;
  }

  /**
   * @param hostTokenOption new value of {@link #gethostTokenOption}.
   */
  public void setHostTokenOption(StringSearchConfigTo hostTokenOption) {

    this.hostTokenOption = hostTokenOption;
  }

  /**
   * @return emailOption
   */
  public StringSearchConfigTo getEmailOption() {

    return this.emailOption;
  }

  /**
   * @param emailOption new value of {@link #getemailOption}.
   */
  public void setEmailOption(StringSearchConfigTo emailOption) {

    this.emailOption = emailOption;
  }

  /**
   * @return bookingTokenOption
   */
  public StringSearchConfigTo getBookingTokenOption() {

    return this.bookingTokenOption;
  }

  /**
   * @param bookingTokenOption new value of {@link #getbookingTokenOption}.
   */
  public void setBookingTokenOption(StringSearchConfigTo bookingTokenOption) {

    this.bookingTokenOption = bookingTokenOption;
  }

  /**
   * @return orderstatus
   */
  public Long[] getOrderstatus() {

    return this.orderstatus;
  }

  /**
   * @param orderstatus new value of {@link #getOrderstatus}.
   */
  public void setOrderstatus(Long[] orderstatus) {

    this.orderstatus = orderstatus;
  }

  /**
   * @return paymentstatus
   */
  public Long[] getPaymentstatus() {

    return this.paymentstatus;
  }

  /**
   * @param paymentstatus new value of {@link #getPaymentstatus}.
   */
  public void setPaymentstatus(Long[] paymentstatus) {

    this.paymentstatus = paymentstatus;
  }

  /**
   * @return paymentstatus
   */
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getName}.
   */
  public void setName(String name) {

    this.name = name;
  }

}
