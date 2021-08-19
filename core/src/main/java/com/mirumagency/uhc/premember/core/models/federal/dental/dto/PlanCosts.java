package com.mirumagency.uhc.premember.core.models.federal.dental.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanCosts {

	private PlanCosts.Cost deductible;
	private PlanCosts.CostString annualMaximum;

	public PlanCosts(PlanCosts.Cost deductible, PlanCosts.CostString annualMaximum) {
		this.deductible = deductible;
		this.annualMaximum = annualMaximum;
	}

	public static PlanCosts empty() {
		return new PlanCosts(new PlanCosts.Cost(0d, 0d), new PlanCosts.CostString("Not Applicable", "Not Applicable"));
	}

	@Getter
	@Setter
	public static class Cost {
		public Cost(Double individual, Double family) {
			this.individual = individual;
			this.family = family;
		}
		private Double individual;
		private Double family;
	}

	@Getter
	@Setter
	public static class CostString {
		public CostString(String individual, String family) {
			this.individual = individual;
			this.family = family;
		}
		private String individual;
		private String family;
	}
}
