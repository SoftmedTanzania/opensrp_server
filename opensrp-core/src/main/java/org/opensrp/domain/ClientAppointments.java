package org.opensrp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "client_appointments")
public class ClientAppointments implements Serializable {

	public static final String tbName = "client_appointments";

	public static final String COL_HEALTH_FACILITY_CLIENT_ID = "health_facility_client_id";

	public static final String COL_APPOINTMENT_ID = "id";

	public static final String COL_APPOINTMENT_DATE = "appointment_date";

	public static final String COL_IS_CANCELLED = "is_cancelled";

	public static final String COL_STATUS = "status";

	public static final String COL_APPOINTMENT_TYPE = "appointment_type";

	public static final String COL_FOLLOWUP_REFERRAL_ID = "followup_referral_id";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";

	@GeneratedValue
	@Column(name = COL_APPOINTMENT_ID, unique = true, nullable = false, insertable = false, updatable = false)
	private Long appointment_id;

	@Id
	@ManyToOne
	@JoinColumn(name= COL_HEALTH_FACILITY_CLIENT_ID)
	private HealthFacilitiesReferralClients healthFacilitiesReferralClients;


	@Id
	@Column(name = COL_APPOINTMENT_DATE)
	private Date appointmentDate;

	@Column(name = COL_IS_CANCELLED)
	private boolean isCancelled;


	@ManyToOne
	@JoinColumn(name=COL_STATUS,referencedColumnName = Status.COL_STATUS_ID)
	private Status status;


	@ManyToOne
	@JoinColumn(name=COL_APPOINTMENT_TYPE,referencedColumnName = AppointmentType.COL_ID)
	private AppointmentType appointmentType;


	@ManyToOne
	@JoinColumn(name=COL_FOLLOWUP_REFERRAL_ID,referencedColumnName = ClientReferrals.COL_REFERRAL_ID)
	private ClientReferrals clientReferrals;


	@Column(name = COL_CREATED_AT, columnDefinition = "TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = COL_UPDATED_AT, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	public HealthFacilitiesReferralClients getHealthFacilitiesReferralClients() {
		return healthFacilitiesReferralClients;
	}

	public void setHealthFacilitiesReferralClients(HealthFacilitiesReferralClients healthFacilitiesReferralClients) {
		this.healthFacilitiesReferralClients = healthFacilitiesReferralClients;
	}

	public Long getAppointment_id() {
		return appointment_id;
	}

	public void setAppointment_id(Long appointment_id) {
		this.appointment_id = appointment_id;
	}


	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public AppointmentType getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(AppointmentType appointmentType) {
		this.appointmentType = appointmentType;
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

	public ClientReferrals getClientReferrals() {
		return clientReferrals;
	}

	public void setClientReferrals(ClientReferrals clientReferrals) {
		this.clientReferrals = clientReferrals;
	}
}
