package org.opensrp.dto.report;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class MaleFemaleCountObject {
    @JsonProperty
    private Long male;
    @JsonProperty
    private Long female;

    public Long getMale() {
        return male;
    }

    public void setMale(Long male) {
        this.male = male;
    }

    public Long getFemale() {
        return female;
    }

    public void setFemale(Long female) {
        this.female = female;
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

