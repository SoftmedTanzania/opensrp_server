package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.Map;

public class CTCPatientsAppointmesDTO {

    @JsonProperty
    private Date RowVersion;

    @JsonProperty
    private String Cancelled;

    @JsonProperty
    private Date DateOfAppointment;

    public Date getRowVersion() {
        return RowVersion;
    }

    public void setRowVersion(Date rowVersion) {
        RowVersion = rowVersion;
    }

    public String getCancelled() {
        return Cancelled;
    }

    public void setCancelled(String cancelled) {
        Cancelled = cancelled;
    }

    public Date getDateOfAppointment() {
        return DateOfAppointment;
    }

    public void setDateOfAppointment(Date dateOfAppointment) {
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
