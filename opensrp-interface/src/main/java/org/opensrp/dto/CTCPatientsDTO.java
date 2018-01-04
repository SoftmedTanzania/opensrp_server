package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.Map;

public class CTCPatientsDTO {
	@JsonProperty
	private String firstName;

	@JsonProperty
	private String middleName;

	@JsonProperty
	private String ctc_number;

	@JsonProperty
	private String contact;

	@JsonProperty
	private Long dateOfBirth;

	@JsonProperty
	private String surname;

	@JsonProperty
	private String gender;

	@JsonProperty
	private Long dateOfDeath;


	@JsonProperty
	private boolean hivStatus;




	@JsonProperty
	private String healthFacilityCode;

	@JsonProperty
	private CTCPatientsAppointmesDTO[] appointments;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getCtc_number() {
		return ctc_number;
	}

	public void setCtc_number(String ctc_number) {
		this.ctc_number = ctc_number;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Long getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Long dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Long dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public CTCPatientsAppointmesDTO[] getAppointments() {
		return appointments;
	}

	public void setAppointments(CTCPatientsAppointmesDTO[] appointments) {
		this.appointments = appointments;
	}

	public boolean isHivStatus() {
		return hivStatus;
	}

	public void setHivStatus(boolean hivStatus) {
		this.hivStatus = hivStatus;
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

	public String getHealthFacilityCode() {
		return healthFacilityCode;
	}

	public void setHealthFacilityCode(String healthFacilityCode) {
		this.healthFacilityCode = healthFacilityCode;
	}
}
