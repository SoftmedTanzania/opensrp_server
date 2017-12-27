package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_patient_referral")
public class PatientReferral {

	public static final String tbName = "tbl_patient_referral";

	public static final String COL_PATIENT_ID = "patient_id";

	public static final String COL_REFERRAL_ID = "referral_id";

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

	public static final String COL_REFERRAL_STATUS= "referral_status";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@GeneratedValue
	@Column(name = "_id")
	private Long id;

	private Long patient_id;

	@ManyToOne
	@JoinColumn(name=COL_PATIENT_ID)
	private Patients patient;


	@Id
	@Column(name = COL_REFERRAL_ID)
	private String referral_id;

	@Column(name = COL_COMMUNITY_BASED_HIV_SERVICE)
	private String communityBasedHivService;

	@Column(name = COL_REFERRAL_REASON)
	private String referralReason;

	@Column(name = COL_SERVICE_ID)
	private int serviceId;

	@Column(name = COL_CTC_NUMBER)
	private String ctcNumber;

	@Column(name = COL_HAS_2WEEKS_COUGH)
	private Boolean has2WeeksCough;

	@Column(name = COL_HAS_BLOOD_COUGH)
	private Boolean hasBloodCough;

	@Column(name = COL_HAS_SEVERE_SWEATING)
	private Boolean hasSevereSweating;

	@Column(name = COL_HAS_FEVER)
	private Boolean hasFever;

	@Column(name = COL_HAD_WEIGHT_LOSS)
	private Boolean hadWeightLoss;

	@Column(name = COL_SERVICE_PROVIDER_UIID)
	private String serviceProviderUIID;

	@Column(name = COL_SERVICE_PROVIDER_GROUP)
	private String serviceProviderGroup;

	@Column(name = COL_VILLAGE_LEADER)
	private String villageLeader;

	@Column(name = COL_REFERRAL_DATE)
	private Date referralDate;

	@Column(name = COL_FACILITY_ID)
	private String facilityId;

	/*
	 *  0 = new
	 * -1 = rejected/discarded
	 *  1 = complete referral
	 */
	@Column(name = COL_REFERRAL_STATUS)
	private int referralStatus;


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

	public String getReferral_id() {
		return referral_id;
	}

	public void setReferral_id(String referral_id) {
		this.referral_id = referral_id;
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

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getCtcNumber() {
		return ctcNumber;
	}

	public void setCtcNumber(String ctcNumber) {
		this.ctcNumber = ctcNumber;
	}

	public Boolean getHas2WeeksCough() {
		return has2WeeksCough;
	}

	public void setHas2WeeksCough(Boolean has2WeeksCough) {
		this.has2WeeksCough = has2WeeksCough;
	}

	public Boolean getHasBloodCough() {
		return hasBloodCough;
	}

	public void setHasBloodCough(Boolean hasBloodCough) {
		this.hasBloodCough = hasBloodCough;
	}

	public Boolean getHasSevereSweating() {
		return hasSevereSweating;
	}

	public void setHasSevereSweating(Boolean hasSevereSweating) {
		this.hasSevereSweating = hasSevereSweating;
	}

	public Boolean getHasFever() {
		return hasFever;
	}

	public void setHasFever(Boolean hasFever) {
		this.hasFever = hasFever;
	}

	public Boolean getHadWeightLoss() {
		return hadWeightLoss;
	}

	public void setHadWeightLoss(Boolean hadWeightLoss) {
		this.hadWeightLoss = hadWeightLoss;
	}

	public String getServiceProviderUIID() {
		return serviceProviderUIID;
	}

	public void setServiceProviderUIID(String serviceProviderUIID) {
		this.serviceProviderUIID = serviceProviderUIID;
	}

	public String getServiceProviderGroup() {
		return serviceProviderGroup;
	}

	public void setServiceProviderGroup(String serviceProviderGroup) {
		this.serviceProviderGroup = serviceProviderGroup;
	}

	public String getVillageLeader() {
		return villageLeader;
	}

	public void setVillageLeader(String villageLeader) {
		this.villageLeader = villageLeader;
	}

	public Date getReferralDate() {
		return referralDate;
	}

	public void setReferralDate(Date referralDate) {
		this.referralDate = referralDate;
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public int getReferralStatus() {
		return referralStatus;
	}

	public void setReferralStatus(int referralStatus) {
		this.referralStatus = referralStatus;
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
