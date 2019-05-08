package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class CHWReferralsListDTO {
    @JsonProperty
    private String service_provider_uuid;

    @JsonProperty
    private String client_name;


    @JsonProperty
    private String facility_name;

    @JsonProperty
    private String referral_status;


	public String getService_provider_uuid() {
		return service_provider_uuid;
	}

	public void setService_provider_uuid(String service_provider_uuid) {
		this.service_provider_uuid = service_provider_uuid;
	}

	public String getFacility_name() {
		return facility_name;
	}

	public void setFacility_name(String facility_name) {
		this.facility_name = facility_name;
	}

	public String getReferral_status() {
		return referral_status;
	}

	public void setReferral_status(String referral_status) {
		this.referral_status = referral_status;
	}

	public String getClient_name() {
		return client_name;
	}

	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}

	@Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

