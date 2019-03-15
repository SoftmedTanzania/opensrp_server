package org.opensrp.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "push_notifications_users")
public class GooglePushNotificationsUsers {

	public static final String tbName = "push_notifications_users";

	public static final String COL_USER_UUID = "user_uuid";

	public static final String COL_GOOGLE_PUSH_NOTIFICATION_TOKEN = "google_push_notification_token";

	public static final String COL_FACILITY_UUID = "facility_uuid";

	public static final String COL_USER_TYPE= "user_type";

	public static final String COL_CREATED_AT = "created_at";

	public static final String COL_UPDATED_AT = "updated_at";


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "_id")
	private Long id;

	@Column(name = COL_USER_UUID)
	private String userUuid;

	@Column(name = COL_GOOGLE_PUSH_NOTIFICATION_TOKEN,unique = true)
	private String googlePushNotificationToken;

	@ManyToOne
	@JoinColumn(name= COL_FACILITY_UUID, referencedColumnName = HealthFacilities.COL_OPENMRS_UUID)
	private HealthFacilities healthFacilities;

	@Column(name = COL_USER_TYPE)
	private int userType;

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

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getGooglePushNotificationToken() {
		return googlePushNotificationToken;
	}

	public void setGooglePushNotificationToken(String googlePushNotificationToken) {
		this.googlePushNotificationToken = googlePushNotificationToken;
	}

	public HealthFacilities getHealthFacilities() {
		return healthFacilities;
	}

	public void setHealthFacilities(HealthFacilities healthFacilities) {
		this.healthFacilities = healthFacilities;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
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
