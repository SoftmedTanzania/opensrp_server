package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.Map;

public class PatientsDTO {
    @JsonProperty
    private String patientId;

    @JsonProperty
    private String patientFirstName;

    @JsonProperty
    private String phoneNumber;

    @JsonProperty
    private String ward;

    @JsonProperty
    private String village;

    @JsonProperty
    private String hamlet;

    @JsonProperty
    private Date dateOfBirth;

    @JsonProperty
    private String patientSurname;

    @JsonProperty
    private String gender;

    @JsonProperty
    private Date dateOfDeath;

    public PatientsDTO() {
    }

    public PatientsDTO(String patientId, String patientFirstName, String phoneNumber, String ward, String village, String hamlet, Date dateOfBirth, String patientSurname, String gender, Date dateOfDeath) {
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.phoneNumber = phoneNumber;
        this.ward = ward;
        this.village = village;
        this.hamlet = hamlet;
        this.dateOfBirth = dateOfBirth;
        this.patientSurname = patientSurname;
        this.gender = gender;
        this.dateOfDeath = dateOfDeath;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getHamlet() {
        return hamlet;
    }

    public void setHamlet(String hamlet) {
        this.hamlet = hamlet;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
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
