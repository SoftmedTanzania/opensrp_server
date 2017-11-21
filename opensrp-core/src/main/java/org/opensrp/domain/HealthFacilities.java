package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_health_facilities")
public class HealthFacilities {

	public static final String tbName = "tbl_health_facilities";

	public static final String COL_FACILITY_ID = "facility_id";

	public static final String COL_FACILITY_NAME = "facility_name";

	public static final String COL_FACILITY_CODE = "facility_code";

	public static final String COL_PARENT_ID = "parent_id";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";

	@Id
	@GeneratedValue
	@Column(name = "_id")
	private Long id;

	@Column(name = COL_FACILITY_ID)
	private String facilityId;

	@Column(name = COL_FACILITY_NAME)
	private String facilityName;

	@Column(name = COL_FACILITY_CODE)
	private String facilityCode;

	@Column(name = COL_PARENT_ID)
	private String parentId;



	@Column(name = COL_CREATED_AT, columnDefinition = "DATETIME")
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

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getFacilityCode() {
		return facilityCode;
	}

	public void setFacilityCode(String facilityCode) {
		this.facilityCode = facilityCode;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
