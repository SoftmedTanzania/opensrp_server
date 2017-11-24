package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_health_facility_referral")
public class HealthFacilityReferral {

	public static final String tbName = "tbl_health_facility_referral";

	public static final String COL_COMMUNITY_BASED_HIV_SERVICE = "community_based_hiv_service";

	public static final String COL_REFERRAL_REASON = "referral_reason";

	public static final String COL_SERVICE_ID = "referral_service_id";

	public static final String COL_CTC_NUMBER = "ctc_number";

	public static final String COL_HAS_2WEEKS_COUGH = "has_2Week_cough";

	public static final String COL_HAS_BLOOD_COUGH= "has_blood_cough";

	public static final String COL_HAS_SEVERE_SWEATING = "has_severe_sweating";

	public static final String COL_HAS_FEVER = "has_fever";

	public static final String COL_HAD_WEIGHT_LOSS = "had_weight_loss";

	public static final String COL_FACILITY_ID = "facility_id";

	public static final String COL_REFERRAL_DATE= "referral_date";

	public static final String COL_SERVICE_PROVIDER_UIID= "service_provider_uiid";

	public static final String COL_SERVICE_PROVIDER_GROUP= "service_provider_group";

	public static final String COL_VILLAGE_LEADER= "village_leader";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";

	@Id
	@GeneratedValue
	@Column(name = "_id")
	private Long id;

	@Column(name = COL_COMMUNITY_BASED_HIV_SERVICE)
	private String communityBasedHivService;

	@Column(name = COL_REFERRAL_REASON)
	private String referralReason;

	@Column(name = COL_SERVICE_ID)
	private String serviceId;

	@Column(name = COL_CTC_NUMBER)
	private String ctcNumber;

	@Column(name = COL_HAS_2WEEKS_COUGH)
	private String has2WeeksCough;

	@Column(name = COL_HAS_BLOOD_COUGH)
	private String hasBloodCough;

	@Column(name = COL_HAS_SEVERE_SWEATING)
	private String hasSevereSweating;

	@Column(name = COL_HAS_FEVER)
	private Date hasFever;

	@Column(name = COL_HAD_WEIGHT_LOSS)
	private String hadWeightLoss;

	@Column(name = COL_SERVICE_PROVIDER_UIID)
	private Date serviceProviderUIID;

	@Column(name = COL_SERVICE_PROVIDER_GROUP)
	private Date serviceProviderGroup;

	@Column(name = COL_VILLAGE_LEADER)
	private Date villageLeader;

	@Column(name = COL_REFERRAL_DATE)
	private Date referralDate;

	@Column(name = COL_FACILITY_ID)
	private Date facilityId;


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

	public String getCommunityBasedHivService() {
		return communityBasedHivService;
	}

	public void setCommunityBasedHivService(String communityBasedHivService) {
		this.communityBasedHivService = communityBasedHivService;
	}

	public String getReferralReason() {
		return referralReason;
	}

	public void setReferralReason(String referralReason) {
		this.referralReason = referralReason;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getCtcNumber() {
		return ctcNumber;
	}

	public void setCtcNumber(String ctcNumber) {
		this.ctcNumber = ctcNumber;
	}

	public String getHas2WeeksCough() {
		return has2WeeksCough;
	}

	public void setHas2WeeksCough(String has2WeeksCough) {
		this.has2WeeksCough = has2WeeksCough;
	}

	public String getHasBloodCough() {
		return hasBloodCough;
	}

	public void setHasBloodCough(String hasBloodCough) {
		this.hasBloodCough = hasBloodCough;
	}

	public String getHasSevereSweating() {
		return hasSevereSweating;
	}

	public void setHasSevereSweating(String hasSevereSweating) {
		this.hasSevereSweating = hasSevereSweating;
	}

	public Date getHasFever() {
		return hasFever;
	}

	public void setHasFever(Date hasFever) {
		this.hasFever = hasFever;
	}

	public String getHadWeightLoss() {
		return hadWeightLoss;
	}

	public void setHadWeightLoss(String hadWeightLoss) {
		this.hadWeightLoss = hadWeightLoss;
	}

	public Date getServiceProviderUIID() {
		return serviceProviderUIID;
	}

	public void setServiceProviderUIID(Date serviceProviderUIID) {
		this.serviceProviderUIID = serviceProviderUIID;
	}

	public Date getServiceProviderGroup() {
		return serviceProviderGroup;
	}

	public void setServiceProviderGroup(Date serviceProviderGroup) {
		this.serviceProviderGroup = serviceProviderGroup;
	}

	public Date getVillageLeader() {
		return villageLeader;
	}

	public void setVillageLeader(Date villageLeader) {
		this.villageLeader = villageLeader;
	}

	public Date getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
	}

	public Date getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(Date facilityId) {
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
