package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.Map;

public class CTCPatientsDTO {
    @JsonProperty
    private String patientId;

    @JsonProperty
    private String patientFirstName;

    @JsonProperty
    private String contacts;

    @JsonProperty
    private Date dateOfBirth;

    @JsonProperty
    private String patientSurname;

    @JsonProperty
    private String gender;

    @JsonProperty
    private String transferInId;

    @JsonProperty
    private Date dateOfFirstPositiveHIVTest;

    @JsonProperty
    private Date dateOfConfirmedHIVPositive;

    @JsonProperty
    private Date dateOfDeath;

    public CTCPatientsDTO(String patientId, String patientFirstName, String contacts, Date dateOfBirth, String patientSurname, String gender, String transferInId, Date dateOfFirstPositiveHIVTest, Date dateOfConfirmedHIVPositive, Date dateOfDeath) {
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.contacts = contacts;
        this.dateOfBirth = dateOfBirth;
        this.patientSurname = patientSurname;
        this.gender = gender;
        this.transferInId = transferInId;
        this.dateOfFirstPositiveHIVTest = dateOfFirstPositiveHIVTest;
        this.dateOfConfirmedHIVPositive = dateOfConfirmedHIVPositive;
        this.dateOfDeath = dateOfDeath;
    }

    public CTCPatientsDTO() {
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTransferInId() {
        return transferInId;
    }

    public void setTransferInId(String transferInId) {
        this.transferInId = transferInId;
    }

    public Date getDateOfFirstPositiveHIVTest() {
        return dateOfFirstPositiveHIVTest;
    }

    public void setDateOfFirstPositiveHIVTest(Date dateOfFirstPositiveHIVTest) {
        this.dateOfFirstPositiveHIVTest = dateOfFirstPositiveHIVTest;
    }

    public Date getDateOfConfirmedHIVPositive() {
        return dateOfConfirmedHIVPositive;
    }

    public void setDateOfConfirmedHIVPositive(Date dateOfConfirmedHIVPositive) {
        this.dateOfConfirmedHIVPositive = dateOfConfirmedHIVPositive;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
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
