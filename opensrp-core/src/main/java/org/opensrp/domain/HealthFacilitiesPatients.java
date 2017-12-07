package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_health_facility_patients")
public class HealthFacilitiesPatients {

	public static final String tbName = "tbl_health_facility_patients";

	public static final String COL_PATIENT_ID = "patient_id";

	public static final String COL_FACILITY_ID = "facility_id";

	public static final String COL_CTC_NUMBER = "ctc_number";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@GeneratedValue
	@Column(name = "_id")
	private Long id;

	@Column(name = COL_PATIENT_ID)
	private Long patient_id;

	@Id
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(Long patient_id) {
		this.patient_id = patient_id;
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
