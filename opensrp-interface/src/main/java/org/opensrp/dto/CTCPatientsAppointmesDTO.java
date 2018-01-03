package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.Map;

public class CTCPatientsAppointmesDTO {

    @JsonProperty
    private Long RowVersion;

    @JsonProperty
    private boolean Cancelled;

    @JsonProperty
    private Long DateOfAppointment;

    public Long getRowVersion() {
        return RowVersion;
    }

    public void setRowVersion(Long rowVersion) {
        RowVersion = rowVersion;
    }

    public boolean getCancelled() {
        return Cancelled;
    }

    public void setCancelled(boolean cancelled) {
        Cancelled = cancelled;
    }

    public Long getDateOfAppointment() {
        return DateOfAppointment;
    }

    public void setDateOfAppointment(Long dateOfAppointment) {
        DateOfAppointment = dateOfAppointment;
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