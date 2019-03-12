package org.opensrp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_patient_referral_indicator")
public class PatientReferralIndicators implements Serializable {

	public static final String tbName = "tbl_patient_referral_indicator";

	public static final String COL_PATIENT_REFERRAL_INDICATOR_ID = "patient_referral_indicator_id";

	public static final String COL_REFERRAL_ID = "referral_id";

	public static final String COL_REFERRAL_SERVICE_INDICATOR_ID = "service_indicator_id";

	public static final String COL_IS_ACTIVE = "is_active";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@Id
	@GeneratedValue
	@Column(name = COL_PATIENT_REFERRAL_INDICATOR_ID)
	private Long patientReferralIndicatorId;

	@ManyToOne
	@JoinColumn(name=COL_REFERRAL_ID)
	private PatientReferral patientReferral;

	@ManyToOne
	@JoinColumn(name=COL_REFERRAL_ID)
	private ServiceIndicator serviceIndicator;

	@Column(name = COL_REFERRAL_SERVICE_INDICATOR_ID)
	private Long referralServiceIndicatorId;

	@Column(name = COL_IS_ACTIVE)
	private boolean isActive;

	@Column(name = COL_CREATED_AT, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = COL_UPDATED_AT, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;


	public Long getPatientReferralIndicatorId() {
		return patientReferralIndicatorId;
	}

	public void setPatientReferralIndicatorId(Long patientReferralIndicatorId) {
		this.patientReferralIndicatorId = patientReferralIndicatorId;
	}

	public PatientReferral getPatientReferral() {
		return patientReferral;
	}

	public void setPatientReferral(PatientReferral patientReferral) {
		this.patientReferral = patientReferral;
	}

	public ServiceIndicator getServiceIndicator() {
		return serviceIndicator;
	}

	public void setServiceIndicator(ServiceIndicator serviceIndicator) {
		this.serviceIndicator = serviceIndicator;
	}

	public Long getReferralServiceIndicatorId() {
		return referralServiceIndicatorId;
	}

	public void setReferralServiceIndicatorId(Long referralServiceIndicatorId) {
		this.referralServiceIndicatorId = referralServiceIndicatorId;
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
