package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_referral_service_indicator")
public class ReferralServiceIndicator {

	public static final String tbName = "tbl_referral_service_indicator";

	public static final String COL_REFERRAL_SERVICE_INDICATOR_ID = "referral_service_indicator_id";

	public static final String COL_REFERRAL_SERVICE_ID = "referral_service_id";

	public static final String COL_REFERRAL_INDICATOR_ID= "referral_indicator_id";

	public static final String COL_IS_ACTIVE = "is_active";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";

	@Id
	@GeneratedValue
	@Column(name = COL_REFERRAL_SERVICE_INDICATOR_ID)
	private Long referralServiceIndicatorId;

	@Id
	@ManyToOne
	@JoinColumn(name= COL_REFERRAL_SERVICE_ID)
	private ReferralService referralService;

	@Id
	@ManyToOne
	@JoinColumn(name= COL_REFERRAL_INDICATOR_ID)
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
}
