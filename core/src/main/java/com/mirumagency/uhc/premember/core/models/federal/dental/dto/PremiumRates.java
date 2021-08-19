package com.mirumagency.uhc.premember.core.models.federal.dental.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter
public class PremiumRates {

	@Accessors(chain = true)
	@Setter
	private String planType;

	private final Rate selfOnly;
	private final Rate selfPlusOne;
	private final Rate selfAndFamily;

	@Builder
	@Getter
	public static class Rate {
		private final Double biWeekly;
		private final Double monthly;
	}
}
