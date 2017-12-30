package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class TBEncounterDTO {
    @JsonProperty
	private Long id;

	@JsonProperty
	private Long tbPatientId;

	@JsonProperty
	private String makohozi;

	@JsonProperty
	private Long appointmentId;

	@JsonProperty
	private int encounterMonth;

	@JsonProperty
	private boolean hasFinishedPreviousMonthMedication;

	@JsonProperty
	private Date scheduledDate;


	@JsonProperty
	private Date medicationDate;

	@JsonProperty
	private boolean medicationStatus;


	@JsonProperty
	private Date createdAt;

	@JsonProperty
	private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTbPatientId() {
		return tbPatientId;
	}

	public void setTbPatientId(Long tbPatientId) {
		this.tbPatientId = tbPatientId;
	}

	public String getMakohozi() {
		return makohozi;
	}

	public void setMakohozi(String makohozi) {
		this.makohozi = makohozi;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public int getEncounterMonth() {
		return encounterMonth;
	}

	public void setEncounterMonth(int encounterMonth) {
		this.encounterMonth = encounterMonth;
	}

	public boolean isHasFinishedPreviousMonthMedication() {
		return hasFinishedPreviousMonthMedication;
	}

	public void setHasFinishedPreviousMonthMedication(boolean hasFinishedPreviousMonthMedication) {
		this.hasFinishedPreviousMonthMedication = hasFinishedPreviousMonthMedication;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Date getMedicationDate() {
		return medicationDate;
	}

	public void setMedicationDate(Date medicationDate) {
		this.medicationDate = medicationDate;
	}

	public boolean isMedicationStatus() {
		return medicationStatus;
	}

	public void setMedicationStatus(boolean medicationStatus) {
		this.medicationStatus = medicationStatus;
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
