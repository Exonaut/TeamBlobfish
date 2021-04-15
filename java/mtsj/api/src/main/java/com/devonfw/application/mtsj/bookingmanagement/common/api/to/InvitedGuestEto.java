package com.devonfw.application.mtsj.bookingmanagement.common.api.to;

import java.time.Instant;

import com.devonfw.application.mtsj.bookingmanagement.common.api.InvitedGuest;
import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * Entity transport object of InvitedGuest
 */
public class InvitedGuestEto extends AbstractEto implements InvitedGuest {

	private static final long serialVersionUID = 1L;

	private Long bookingId;

	private String guestToken;

	private String email;

	// set primitive data type
	private boolean accepted;

	private Instant modificationDate;

	@Override
	public Long getBookingId() {

		return this.bookingId;
	}

	@Override
	public void setBookingId(Long bookingId) {

		this.bookingId = bookingId;
	}

	@Override
	public String getGuestToken() {

		return this.guestToken;
	}

	@Override
	public void setGuestToken(String guestToken) {

		this.guestToken = guestToken;
	}

	@Override
	public String getEmail() {

		return this.email;
	}

	@Override
	public void setEmail(String email) {

		this.email = email;
	}

	// set primitive data type
	@Override
	public boolean getAccepted() {

		return this.accepted;
	}

	// set primitive data type
	@Override
	public void setAccepted(boolean accepted) {

		this.accepted = accepted;
	}

	@Override
	public Instant getModificationDate() {

		return this.modificationDate;
	}

	@Override
	public void setModificationDate(Instant modificationDate) {

		this.modificationDate = modificationDate;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = super.hashCode();

		result = prime * result + ((this.bookingId == null) ? 0 : this.bookingId.hashCode());
		result = prime * result + ((this.guestToken == null) ? 0 : this.guestToken.hashCode());
		result = prime * result + ((this.email == null) ? 0 : this.email.hashCode());
		// cast primitive type field into object type to generate a hash code
		result = prime * result + ((this.accepted == false) ? 0 : ((Boolean) this.accepted).hashCode());
		result = prime * result + ((this.modificationDate == null) ? 0 : this.modificationDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		// class check will be done by super type EntityTo!
		if (!super.equals(obj)) {
			return false;
		}
		InvitedGuestEto other = (InvitedGuestEto) obj;

		if (this.bookingId == null) {
			if (other.bookingId != null) {
				return false;
			}
		} else if (!this.bookingId.equals(other.bookingId)) {
			return false;
		}
		if (this.guestToken == null) {
			if (other.guestToken != null) {
				return false;
			}
		} else if (!this.guestToken.equals(other.guestToken)) {
			return false;
		}
		if (this.email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!this.email.equals(other.email)) {
			return false;
		}
		// remove null checks
		if (this.accepted == false) {
			if (other.accepted != false) {
				return false;
			}
		} else if (!this.accepted == (other.accepted)) {
			return false;
		}
		if (this.modificationDate == null) {
			if (other.modificationDate != null) {
				return false;
			}
		} else if (!this.modificationDate.equals(other.modificationDate)) {
			return false;
		}
		return true;
	}

}
