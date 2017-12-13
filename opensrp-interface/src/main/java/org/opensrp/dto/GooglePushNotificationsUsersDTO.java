package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

public class GooglePushNotificationsUsersDTO {
    @JsonProperty
    private Long id;

    @JsonProperty
    private String userUiid;

    @JsonProperty
    private String googlePushNotificationToken;

    @JsonProperty
    private String facilityUiid;

    @JsonProperty
    private Date createdAt;

    @JsonProperty
    private Date updatedAt;

    public GooglePushNotificationsUsersDTO(Long id, String userUiid, String googlePushNotificationToken, String facilityUiid, Date createdAt, Date updatedAt) {
        this.id = id;
        this.userUiid = userUiid;
        this.googlePushNotificationToken = googlePushNotificationToken;
        this.facilityUiid = facilityUiid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserUiid() {
        return userUiid;
    }

    public void setUserUiid(String userUiid) {
        this.userUiid = userUiid;
    }

    public String getGooglePushNotificationToken() {
        return googlePushNotificationToken;
    }

    public void setGooglePushNotificationToken(String googlePushNotificationToken) {
        this.googlePushNotificationToken = googlePushNotificationToken;
    }

    public String getFacilityUiid() {
        return facilityUiid;
    }

    public void setFacilityUiid(String facilityUiid) {
        this.facilityUiid = facilityUiid;
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
