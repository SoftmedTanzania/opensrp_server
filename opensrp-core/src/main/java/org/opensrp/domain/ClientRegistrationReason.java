package org.opensrp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "registration_reasons")
public class ClientRegistrationReason implements Serializable {

	public static final String tbName = "registration_reasons";

	public static final String COL_REGISTRATION__ID = "registration_id";

	public static final String COL_DESC_EN = "desc_en";

	public static final String COL_DESC_SW = "desc_sw";

	public static final String COL_APPLICABLE_TO_MEN = "applicable_to_men";

	public static final String COL_APPLICABLE_TO_WOMEN = "applicable_to_women";

	public static final String COL_IS_ACTIVE = "is_active";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@Id
	@GeneratedValue
	@Column(name = COL_REGISTRATION__ID)
	private  int registrationId;

	@Column(name = COL_DESC_EN)
	private String descEn;

	@Column(name = COL_DESC_SW)
	private String descSw;

	@Column(name = COL_IS_ACTIVE)
	private boolean isActive;

	@Column(name = COL_APPLICABLE_TO_MEN)
	private boolean applicableToMen;

	@Column(name = COL_APPLICABLE_TO_WOMEN)
	private boolean applicableToWomen;

	@Column(name = COL_CREATED_AT, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = COL_UPDATED_AT, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	public int getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(int registrationId) {
		this.registrationId = registrationId;
	}

	public String getDescEn() {
		return descEn;
	}

	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	public String getDescSw() {
		return descSw;
	}

	public void setDescSw(String descSw) {
		this.descSw = descSw;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public boolean isApplicableToMen() {
		return applicableToMen;
	}

	public void setApplicableToMen(boolean applicableToMen) {
		this.applicableToMen = applicableToMen;
	}

	public boolean isApplicableToWomen() {
		return applicableToWomen;
	}

	public void setApplicableToWomen(boolean applicableToWomen) {
		this.applicableToWomen = applicableToWomen;
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
