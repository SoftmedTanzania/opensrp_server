package org.opensrp.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
	public class PKReferralServiceIndicator implements Serializable {
	public static final String COL_SERVICE_ID = "service_id";

	public static final String COL_INDICATOR_ID = "indicator_id";


	@ManyToOne
	@JoinColumn(name=COL_SERVICE_ID)
	private ReferralService referralService;

	@ManyToOne
	@JoinColumn(name=COL_INDICATOR_ID)
	private Indicator indicator;

	public ReferralService getReferralService() {
		return referralService;
	}

	public void setReferralService(ReferralService referralService) {
		this.referralService = referralService;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}


	public PKReferralServiceIndicator(ReferralService referralService, Indicator indicator) {
		this.referralService = referralService;
		this.indicator = indicator;
	}

	@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof PKReferralServiceIndicator)) return false;
			PKReferralServiceIndicator that = (PKReferralServiceIndicator) o;
			return Objects.equals(getIndicator(), that.getIndicator()) &&
					Objects.equals(getReferralService(), that.getReferralService());
		}

		@Override
		public int hashCode() {
			return Objects.hash(getIndicator(), getReferralService());
		}
	}