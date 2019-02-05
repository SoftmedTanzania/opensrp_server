package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class CTCPayloadDTO {
	@JsonProperty
	private List<CTCPatientsDTO> ctcPatientsDTOS;

	@JsonProperty
	private String facilityCTC2Id;

	@JsonProperty
	private String facilityUUID;

	public List<CTCPatientsDTO> getCtcPatientsDTOS() {
		return ctcPatientsDTOS;
	}

	public void setCtcPatientsDTOS(List<CTCPatientsDTO> ctcPatientsDTOS) {
		this.ctcPatientsDTOS = ctcPatientsDTOS;
	}

	public String getFacilityCTC2Id() {
		return facilityCTC2Id;
	}

	public void setFacilityCTC2Id(String facilityCTC2Id) {
		this.facilityCTC2Id = facilityCTC2Id;
	}

	public String getFacilityUUID() {
		return facilityUUID;
	}

	public void setFacilityUUID(String facilityUUID) {
		this.facilityUUID = facilityUUID;
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
