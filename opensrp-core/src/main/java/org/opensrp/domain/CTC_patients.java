package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_patients")
public class CTC_patients {

	public static final String tbName = "tbl_patients";

	public static final String COL_PATIENT_ID = "patient_id";

	public static final String COL_PATIENT_FIRST_NAME = "patient_first_name";

	public static final String COL_PATIENT_SURNAME = "patient_surname";

	public static final String COL_CONTACTS = "contacts";

	public static final String COL_DATE_OF_BIRTH = "date_of_birth";

	public static final String COL_GENDER = "gender";

	public static final String COL_TRANSFER_IN_ID= "transfer_in_id";

	public static final String COL_DATE_OF_FIRST_POSITIVE_HIV_TEST= "date_of_first_positive_HIV_test";

	public static final String COL_DATE_OF_CONFIRMED_HIV_POSITIVE= "date_of_confirmed_HIV_positive";

	public static final String COL_DATE_OF_DEATH= "date_of_death";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";

	@Id
	@GeneratedValue
	@Column(name = "_id")
	private Long id;

	@Column(name = COL_PATIENT_ID)
	private String patientId;

	@Column(name = COL_PATIENT_FIRST_NAME)
	private String patientFirstName;

	@Column(name = COL_PATIENT_SURNAME)
	private String patientSurname;

	@Column(name = COL_CONTACTS)
	private String contacts;

	@Column(name = COL_DATE_OF_BIRTH)
	private Date dateOfBirth;

	@Column(name = COL_GENDER)
	private String gender;

	@Column(name = COL_TRANSFER_IN_ID)
	private String transferInId;

	@Column(name = COL_DATE_OF_FIRST_POSITIVE_HIV_TEST)
	private Date dateOfFirstPositiveHIVTest;

	@Column(name = COL_DATE_OF_CONFIRMED_HIV_POSITIVE)
	private Date dateOfConfirmedHIVPositive;

	@Column(name = COL_DATE_OF_DEATH)
	private Date dateOfDeath;


	@Column(name = COL_CREATED_AT, columnDefinition = "DATETIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = COL_UPDATED_AT, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getPatientSurname() {
		return patientSurname;
	}

	public void setPatientSurname(String patientSurname) {
		this.patientSurname = patientSurname;
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

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
