package org.opensrp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "client_referral_indicator")
public class ClientReferralIndicators implements Serializable {

	public static final String tbName = "client_referral_indicator";

	public static final String COL_ID = "id";

	public static final String COL_REFERRAL_ID = "referral_id";
	public static final String COL_SERVICE_ID = "service_id";
	public static final String COL_INDICATOR_ID = "indicator_id";

	public static final String COL_IS_ACTIVE = "is_active";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@Id
	@GeneratedValue
	@Column(name = COL_ID)
	private Long id;

	@ManyToOne
	@JoinColumn(name=COL_REFERRAL_ID)
	private ClientReferrals clientReferrals;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(
					name = COL_SERVICE_ID,
					referencedColumnName = "service_id"),
			@JoinColumn(
					name = COL_INDICATOR_ID,
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
