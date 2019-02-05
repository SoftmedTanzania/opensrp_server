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
	private String facilityCTC2Code;

	@JsonProperty
	private String hfrCode;

	public List<CTCPatientsDTO> getCtcPatientsDTOS() {
		return ctcPatientsDTOS;
	}

	public void setCtcPatientsDTOS(List<CTCPatientsDTO> ctcPatientsDTOS) {
		this.ctcPatientsDTOS = ctcPatientsDTOS;
	}

	public String getFacilityCTC2Code() {
		return facilityCTC2Code;
	}

	public void setFacilityCTC2Code(String facilityCTC2Code) {
		this.facilityCTC2Code = facilityCTC2Code;
	}

	public String getHfrCode() {
		return hfrCode;
	}

	public void setHfrCode(String hfrCode) {
		this.hfrCode = hfrCode;
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
