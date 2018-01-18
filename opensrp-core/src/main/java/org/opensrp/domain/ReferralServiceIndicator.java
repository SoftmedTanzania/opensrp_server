package org.opensrp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tbl_referral_service_indicator")
public class ReferralServiceIndicator implements Serializable {

	public static final String tbName = "tbl_referral_service_indicator";

	public static final String COL_REFERRAL_SERVICE_INDICATOR_ID = "referral_service_indicator_id";

	public static final String COL_SERVICE_ID = "service_id";

	public static final String COL_REFERRAL_INDICATOR_ID= "referral_indicator_id";

	public static final String COL_IS_ACTIVE = "is_active";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@Column(name = COL_REFERRAL_SERVICE_INDICATOR_ID)
	private Long referralServiceIndicatorId;


	@EmbeddedId
	private PK pk;

	@MapsId("serviceId") //references EmbeddedId's property
	@JoinColumn(name = COL_SERVICE_ID, referencedColumnName = COL_SERVICE_ID,insertable=false, updatable=false)
	@ManyToOne
	private ReferralService referralService;


	@MapsId("indicatorId") //references EmbeddedId's property
	@JoinColumn(name = COL_REFERRAL_INDICATOR_ID, referencedColumnName = COL_REFERRAL_INDICATOR_ID,insertable=false, updatable=false)
	@ManyToOne
	private ReferralIndicator referralIndicator;


	@Column(name = COL_IS_ACTIVE)
	private boolean isActive;

	@Column(name = COL_CREATED_AT, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = COL_UPDATED_AT, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	public Long getReferralServiceIndicatorId() {
		return referralServiceIndicatorId;
	}

	public void setReferralServiceIndicatorId(Long referralServiceIndicatorId) {
		this.referralServiceIndicatorId = referralServiceIndicatorId;
	}

	public ReferralService getReferralService() {
		return referralService;
	}

	public void setReferralService(ReferralService referralService) {
		this.referralService = referralService;
	}

	public ReferralIndicator getReferralIndicator() {
		return referralIndicator;
	}

	public void setReferralIndicator(ReferralIndicator referralIndicator) {
		this.referralIndicator = referralIndicator;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}



	@Embeddable
	public class PK implements Serializable {
		@Column(name = COL_SERVICE_ID)
		private long serviceId;

		@Column(name = COL_REFERRAL_INDICATOR_ID)
		private long indicatorId;


		public long getServiceId() {
			return serviceId;
		}

		public void setServiceId(long serviceId) {
			this.serviceId = serviceId;
		}

		public long getIndicatorId() {
			return indicatorId;
		}

		public void setIndicatorId(long indicatorId) {
			this.indicatorId = indicatorId;
		}

		public PK(long indicatorId, long serviceId) {
			this.serviceId = serviceId;
			this.indicatorId = indicatorId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof PK)) return false;
			PK that = (PK) o;
			return Objects.equals(getIndicatorId(), that.getIndicatorId()) &&
					Objects.equals(getServiceId(), that.getServiceId());
		}

		@Override
		public int hashCode() {
			return Objects.hash(getIndicatorId(), getServiceId());
		}
	}
}
