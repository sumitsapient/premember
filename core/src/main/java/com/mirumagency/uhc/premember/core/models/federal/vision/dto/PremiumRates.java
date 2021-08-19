package com.mirumagency.uhc.premember.core.models.federal.vision.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PremiumRates {
	private final Rate selfOnly;
	private final Rate selfPlusOne;
	private final Rate selfAndFamily;

	@Getter
	public static class Rate {
		private final Double biWeekly;
		private final Double monthly;

		public Rate(Double biWeekly, Double monthly) {
			this.biWeekly = biWeekly;
			this.monthly = monthly;
		}
	}
}
