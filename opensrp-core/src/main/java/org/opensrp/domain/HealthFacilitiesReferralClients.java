package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_health_facility_clients")
public class HealthFacilitiesReferralClients {

	public static final String tbName = "tbl_health_facility_clients";

	public static final String COL_HEALTH_FACILITY_CLIENT_ID = "health_facility_client_id";

	public static final String COL_CLIENT_ID = "client_id";

	public static final String COL_FACILITY_ID = "facility_id";

	public static final String COL_CTC_NUMBER = "ctc_number";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@Id
	@GeneratedValue
	@Column(name = COL_HEALTH_FACILITY_CLIENT_ID)
	private Long healthFacilityClientId;

	@ManyToOne
	@JoinColumn(name= COL_CLIENT_ID)
	private ReferralClient patient;

	@Column(name = COL_CTC_NUMBER)
	private String ctcNumber;

	@Column(name = COL_FACILITY_ID)
	private Long facilityId;

	@Column(name = COL_CREATED_AT, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = COL_UPDATED_AT, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	public Long getHealthFacilityClientId() {
		return healthFacilityClientId;
	}

	public void setHealthFacilityClientId(Long healthFacilityClientId) {
		this.healthFacilityClientId = healthFacilityClientId;
	}

	public ReferralClient getPatient() {
		return patient;
	}

	public void setPatient(ReferralClient patient) {
		this.patient = patient;
	}

	public String getCtcNumber() {
		return ctcNumber;
	}

	public void setCtcNumber(String ctcNumber) {
		this.ctcNumber = ctcNumber;
	}

	public Long getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Long facilityId) {
		this.facilityId = facilityId;
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
