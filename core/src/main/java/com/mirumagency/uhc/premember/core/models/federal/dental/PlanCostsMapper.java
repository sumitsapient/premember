package com.mirumagency.uhc.premember.core.models.federal.dental;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PlanCosts;

import java.util.Map;

public class PlanCostsMapper {

	private static final String DEDUCTIBLE_INDIVIDUAL = "deductibleIndividual";
	private static final String DEDUCTIBLE_FAMILY = "deductibleFamily";
	private static final String ANNUAL_MAXIMUM_INDIVIDUAL = "annualMaximumIndividual";
	private static final String ANNUAL_MAXIMUM_FAMILY = "annualMaximumFamily";

	public static PlanCosts map(PlanOption planOption){
		ObjectMapper oMapper = new ObjectMapper();
		Object rates = planOption.getData().get("planCosts");
		Map<String, String> ratesMap = oMapper.convertValue(rates, new TypeReference<Map<String, String>>() {});
		PlanCosts.Cost deductible = new PlanCosts.Cost(
			Double.parseDouble(ratesMap.get(DEDUCTIBLE_INDIVIDUAL)),
			Double.parseDouble(ratesMap.get(DEDUCTIBLE_FAMILY)));
		PlanCosts.CostString annualMaximum = new PlanCosts.CostString(
			ratesMap.get(ANNUAL_MAXIMUM_INDIVIDUAL),
			ratesMap.get(ANNUAL_MAXIMUM_FAMILY));
		return new PlanCosts(deductible, annualMaximum);
	}
}
