package org.opensrp.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class ReferralServiceIndicatorsDTO {
    @JsonProperty
    private Long serviceId;

    @JsonProperty
    private String serviceName;

    @JsonProperty
    private String category;

    @JsonProperty
    private boolean isActive;


    @JsonProperty
    List<ReferralIndicatorDTO> indicators;

    public ReferralServiceIndicatorsDTO(Long serviceId, String serviceName, String category, boolean isActive, List<ReferralIndicatorDTO> indicators) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.category = category;
        this.isActive = isActive;
        this.indicators = indicators;
    }

    public ReferralServiceIndicatorsDTO() {
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ReferralIndicatorDTO> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<ReferralIndicatorDTO> indicators) {
        this.indicators = indicators;
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
