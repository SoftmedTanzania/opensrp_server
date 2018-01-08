package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
import java.util.Date;

public class ReferralsDTO {

    @JsonProperty
    private Long patientId;

    @JsonProperty
    private String communityBasedHivService;

    @JsonProperty
    private String referralReason;

    @JsonProperty
    private int serviceId;

    @JsonProperty
    private String ctcNumber;

    @JsonProperty
    private Boolean has2WeeksCough;

    @JsonProperty
    private Boolean hasBloodCough;

    @JsonProperty
    private Boolean hasSevereSweating;

    @JsonProperty
    private Boolean hasFever;

    @JsonProperty
    private Boolean hadWeightLoss;

    @JsonProperty
    private String serviceProviderUIID;

    @JsonProperty
    private String serviceProviderGroup;

    @JsonProperty
    private String villageLeader;

    @JsonProperty
    private String otherClinicalInformation;

    @JsonProperty
    private String otherNotes;

    @JsonProperty
    private String serviceGivenToPatient;


    @JsonProperty
    private String testResults; //TODO handle its implementation


    @JsonProperty
    private int fromFacilityId;

    @JsonProperty
    private int referralSource;

    @JsonProperty
    private Long referralDate;

    @JsonProperty
    private String facilityId;

    /*
	 *  0 = new
	 * -1 = rejected/discarded
	 *  1 = complete referral
	 */
    @JsonProperty
    private int referralStatus;


    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
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

    public long getReferralDate() {
        return referralDate;
    }

    public void setReferralDate(long referralDate) {
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

    public String getOtherClinicalInformation() {
        return otherClinicalInformation;
    }

    public void setOtherClinicalInformation(String otherClinicalInformation) {
        this.otherClinicalInformation = otherClinicalInformation;
    }

    public String getOtherNotes() {
        return otherNotes;
    }

    public void setOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }

    public String getServiceGivenToPatient() {
        return serviceGivenToPatient;
    }

    public void setServiceGivenToPatient(String serviceGivenToPatient) {
        this.serviceGivenToPatient = serviceGivenToPatient;
    }

    public int getFromFacilityId() {
        return fromFacilityId;
    }

    public void setFromFacilityId(int fromFacilityId) {
        this.fromFacilityId = fromFacilityId;
    }

    public int getReferralSource() {
        return referralSource;
    }

    public void setReferralSource(int referralSource) {
        this.referralSource = referralSource;
    }

    @Override
    public final boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
