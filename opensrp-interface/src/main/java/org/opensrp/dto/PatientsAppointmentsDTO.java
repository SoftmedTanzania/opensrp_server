package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

public class PatientsAppointmentsDTO {
    @JsonProperty
    private Long appointment_id;

    @JsonProperty
    private Long healthFacilityPatientId;


    @JsonProperty
    private Long appointmentDate;

    @JsonProperty
    private boolean isCancelled;

    @JsonProperty
    private int status;

    @JsonProperty
    private Long rowVersion;

    @JsonProperty
    private int appointmentType;


    public Long getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(Long appointment_id) {
        this.appointment_id = appointment_id;
    }

    public Long getHealthFacilityPatientId() {
        return healthFacilityPatientId;
    }

    public void setHealthFacilityPatientId(Long healthFacilityPatientId) {
        this.healthFacilityPatientId = healthFacilityPatientId;
    }

    public long getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(long appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public int getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(int appointmentType) {
        this.appointmentType = appointmentType;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
