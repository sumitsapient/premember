package com.mirumagency.uhc.premember.core.services.federal.repository.jcr;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.mirumagency.uhc.premember.core.domain.federal.PlanName;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.repository.DentalPremiumRatesRepo;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlanNameRepo;
import com.mirumagency.uhc.premember.core.services.migration.Constants;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.http.util.Asserts.notNull;

@Component(service = DentalPremiumRatesRepo.class)
public class DentalPremiumRatesRepoJcr implements DentalPremiumRatesRepo {

	private static final String SELF_ONLY_RATE = "selfOnly";
	private static final String SELF_PLUS_ONE_RATE = "selfPlusOne";
	private static final String SELF_AND_FAMILY_RATE = "selfAndFamily";
	private static final String BI_WEEKLY = "BiWeekly";
	private static final String MONTHLY = "Monthly";

	@Reference
	private ResourceGentleman resourceGentleman;

	@Reference
	private PlanNameRepo planNameRepo;

	@Override
	public List<PremiumRates> loadAll(String regionCode) {
		List<PlanName> planNames = planNameRepo.loadPlanTypes(PlanType.DENTAL);
		return resourceGentleman.withResolver(
				resolver -> resolver
						.getResourceOrThrow(Constants.FEDERAL_DENTAL_REGIONS_PATH)
						.getChild(regionCode)
						.map(regions -> regions.getChildren()
								.filter(plan -> planNames.stream().anyMatch(p -> p.getId().equals(plan.getName()))))
						.map(plans -> plans.map(plan -> plan
								.adaptTo(ContentFragment.class)))
						.map(planContentFragment -> planContentFragment.map(this::loadPremiumRate).collect(Collectors.toList()))
						.orElse(Collections.emptyList()));
	}

	@Override
	public Optional<PremiumRates> load(String regionCode, String dentalPlanType) {
		return resourceGentleman.withResolver(
			resolver -> resolver
				.getResourceOrThrow(Constants.FEDERAL_DENTAL_REGIONS_PATH)
				.getChild(regionCode)
				.flatMap(region -> region.getChild(dentalPlanType))
				.map(plan -> plan.adaptTo(ContentFragment.class))
				.map(this::loadPremiumRate));
	}

	private PremiumRates loadPremiumRate(ContentFragment planContentFragment) {
		notNull(planContentFragment, "planContentFragment");
		return PremiumRates
			.builder()
			.selfOnly(loadRate(planContentFragment, SELF_ONLY_RATE))
			.selfPlusOne(loadRate(planContentFragment, SELF_PLUS_ONE_RATE))
			.selfAndFamily(loadRate(planContentFragment, SELF_AND_FAMILY_RATE))
			.planType(planContentFragment.getName())
			.build();
	}

	private static PremiumRates.Rate loadRate(ContentFragment planContentFragment, String rateType) {
		String premiumBiweeklyField = rateType + BI_WEEKLY;
		ContentElement premiumBiweeklyElement = planContentFragment.getElement(premiumBiweeklyField);
		notNull(premiumBiweeklyElement, premiumBiweeklyField);

		String premiumMonthlyField = rateType + MONTHLY;
		ContentElement premiumMonthlyElement = planContentFragment.getElement(premiumMonthlyField);
		notNull(premiumMonthlyElement, premiumMonthlyField);

		return PremiumRates.Rate
			.builder()
			.biWeekly(Double.valueOf(premiumBiweeklyElement.getContent()))
			.monthly(Double.valueOf(premiumMonthlyElement.getContent()))
			.build();
	}
}
