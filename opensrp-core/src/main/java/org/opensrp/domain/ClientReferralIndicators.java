package org.opensrp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_client_referral_indicator")
public class ClientReferralIndicators implements Serializable {

	public static final String tbName = "tbl_client_referral_indicator";

	public static final String COL_CLIENT_REFERRAL_INDICATOR_ID = "client_referral_indicator_id";

	public static final String COL_REFERRAL_ID = "referral_id";

	public static final String COL_REFERRAL_SERVICE_INDICATOR_ID = "service_indicator_id";

	public static final String COL_IS_ACTIVE = "is_active";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@Id
	@GeneratedValue
	@Column(name = COL_CLIENT_REFERRAL_INDICATOR_ID)
	private Long clientReferralIndicatorId;

	@ManyToOne
	@JoinColumn(name=COL_REFERRAL_ID)
	private ClientReferrals clientReferrals;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(
					name = "service_id",
					referencedColumnName = "service_id"),
			@JoinColumn(
					name = "indicator_id",
					referencedColumnName = "indicator_id")
	})
	private ServiceIndicator serviceIndicator;

	@Column(name = COL_IS_ACTIVE)
	private boolean isActive;

	@Column(name = COL_CREATED_AT, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = COL_UPDATED_AT, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;


	public Long getClientReferralIndicatorId() {
		return clientReferralIndicatorId;
	}

	public void setClientReferralIndicatorId(Long clientReferralIndicatorId) {
		this.clientReferralIndicatorId = clientReferralIndicatorId;
	}

	public ClientReferrals getClientReferrals() {
		return clientReferrals;
	}

	public void setClientReferrals(ClientReferrals clientReferrals) {
		this.clientReferrals = clientReferrals;
	}

	public ServiceIndicator getServiceIndicator() {
		return serviceIndicator;
	}

	public void setServiceIndicator(ServiceIndicator serviceIndicator) {
		this.serviceIndicator = serviceIndicator;
	}


	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
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
