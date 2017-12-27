package org.opensrp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tbl_tb_encounter")
public class TBEncounter implements Serializable {

	public static final String tbName = "tbl_tb_encounter";

	public static final String COL_TB_PATIENT_ID = "tb_patient_id";

	public static final String COL_MAKOHOZI = "makohozi";

	public static final String COL_APPOINTMENT_ID= "appointment_id";

	public static final String COL_ENCOUNTER_MONTH = "encounter_month";

	public static final String COL_SCHEDULED_DATE = "scheduled_date";

	public static final String COL_HAS_FINISHED_PREVIOUS_MONTH_MEDICATION= "has_finished_previous_month_medication";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@GeneratedValue
	@Column(name = "_id")
	private Long id;

	@Id
	@Column(name = COL_TB_PATIENT_ID)
	private Long tbPatientId;

	@Column(name = COL_MAKOHOZI)
	private String makohozi;

	@Id
	@Column(name = COL_APPOINTMENT_ID)
	private Long appointmentId;

	@Column(name = COL_ENCOUNTER_MONTH)
	private int encounterMonth;

	@Column(name = COL_HAS_FINISHED_PREVIOUS_MONTH_MEDICATION)
	private boolean hasFinishedPreviousMonthMedication;

	@Column(name = COL_SCHEDULED_DATE)
	private Date scheduledDate;

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
}
