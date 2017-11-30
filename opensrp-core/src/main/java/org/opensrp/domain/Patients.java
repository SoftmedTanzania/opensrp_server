package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_patients")
public class Patients {

	public static final String tbName = "tbl_referral_patients";

	public static final String COL_PATIENT_ID = "patient_id";

	public static final String COL_PATIENT_FIRST_NAME = "patient_first_name";

	public static final String COL_PATIENT_MIDDLE_NAME = "patient_middle_name";

	public static final String COL_PATIENT_SURNAME = "patient_surname";

	public static final String COL_PHONE_NUMBER = "phone_number";

	public static final String COL_WARD = "ward";

	public static final String COL_VILLAGE = "village";

	public static final String COL_HAMLET = "hamlet";

	public static final String COL_DATE_OF_BIRTH = "date_of_birth";

	public static final String COL_GENDER = "gender";

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


	@Column(name = COL_PATIENT_MIDDLE_NAME)
	private String patientMiddleName;

	@Column(name = COL_PATIENT_SURNAME)
	private String patientSurname;

	@Column(name = COL_PHONE_NUMBER)
	private String phone_number;

	@Column(name = COL_WARD)
	private String ward;

	@Column(name = COL_VILLAGE)
	private String village;

	@Column(name = COL_HAMLET)
	private String hamlet;

	@Column(name = COL_DATE_OF_BIRTH)
	private Date dateOfBirth;

	@Column(name = COL_GENDER)
	private String gender;

	@Column(name = COL_DATE_OF_DEATH)
	private Date dateOfDeath;


	@Column(name = COL_CREATED_AT, columnDefinition = "TIMESTAMP")
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

	public String getPatientMiddleName() {
		return patientMiddleName;
	}

	public void setPatientMiddleName(String patientMiddleName) {
		this.patientMiddleName = patientMiddleName;
	}

	public String getPatientSurname() {
		return patientSurname;
	}

	public void setPatientSurname(String patientSurname) {
		this.patientSurname = patientSurname;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
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
