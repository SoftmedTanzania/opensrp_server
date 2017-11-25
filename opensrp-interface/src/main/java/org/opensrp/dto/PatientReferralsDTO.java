package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

public class PatientReferralsDTO {
    @JsonProperty
    private PatientsDTO patientsDTO;

    @JsonProperty
    private List<ReferralsDTO> referralsDTOS;

    public PatientReferralsDTO(PatientsDTO patientsDTO, List<ReferralsDTO> patientReferralsList) {
        this.patientsDTO = patientsDTO;
        this.referralsDTOS = patientReferralsList;
    }

	public PatientReferralsDTO() {
	}

	public PatientsDTO getPatientsDTO() {
        return patientsDTO;
    }

    public void setPatientsDTO(PatientsDTO patientsDTO) {
        this.patientsDTO = patientsDTO;
    }

    public List<ReferralsDTO> getPatientReferralsList() {
        return referralsDTOS;
    }

    public void setPatientReferralsList(List<ReferralsDTO> patientReferralsList) {
        this.referralsDTOS = patientReferralsList;
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
