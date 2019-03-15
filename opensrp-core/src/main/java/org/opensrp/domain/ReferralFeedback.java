package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "referral_feedback")
public class ReferralFeedback {

	public static final String tbName = "referral_feedback";

	public static final String COL_DESC = "desc_en";

	public static final String COL_DESC_SW = "desc_sw";

	public static final String COL_REFERRAL_TYPE_ID = "referral_type_id";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";

	@Id
	@GeneratedValue
	@Column(name = "_id")
	private Long id;

	@Column(name = COL_DESC)
	private String desc;

	@Column(name = COL_DESC_SW)
	private String descSw;

	@ManyToOne
	@JoinColumn(name= COL_REFERRAL_TYPE_ID, referencedColumnName= ReferralType.COL_REFERRAL_TYPE_ID)
	private ReferralType referralType;


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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDescSw() {
		return descSw;
	}

	public void setDescSw(String descSw) {
		this.descSw = descSw;
	}

	public ReferralType getReferralType() {
		return referralType;
	}

	public void setReferralType(ReferralType referralType) {
		this.referralType = referralType;
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
