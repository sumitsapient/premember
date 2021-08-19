package com.mirumagency.uhc.premember.core.models.federal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

@Model(adaptables = SlingHttpServletRequest.class,
	adapters = PlanBenefitsSummary.class,
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Getter
public class PlanBenefitsSummary {

	private static final String DISPLAY_BENEFIT_1 = "showBenefit1";
	private static final String DISPLAY_BENEFIT_2 = "showBenefit2";
	private static final String DISPLAY_BENEFIT_3 = "showBenefit3";
	private static final String DISPLAY_BENEFIT_4 = "showBenefit4";
	private static final String DISPLAY_BENEFIT_5 = "showBenefit5";
	private static final String DISPLAY_BENEFIT_6 = "showBenefit6";
	private static final String PLAN_BENEFITS_SUMMARY = "planBenefitsSummary";

	@Inject
	private PlansService plansService;

	@SlingObject
	private SlingHttpServletRequest request;

	private boolean showBenefit1;
	private boolean showBenefit2;
	private boolean showBenefit3;
	private boolean showBenefit4;
	private boolean showBenefit5;
	private boolean showBenefit6;

	@PostConstruct
	protected void init() {
		String pageUrl = request.getPathInfo();
			Optional<Plan> plan = plansService.loadPlan(pageUrl);
			plan.ifPresent(this::mapToModel);
		}

	private void mapToModel(Plan plan) {
		if (plan.getSelectedOption() != null) {
			PlanOption selectedOption = plan.getSelectedOption();
			Map<String, Object > data = selectedOption.getData();
			ObjectMapper objectMapper = new ObjectMapper();
			if (data.containsKey(PLAN_BENEFITS_SUMMARY)) {
				Object summary = data.get(PLAN_BENEFITS_SUMMARY);
				Map<String , Object> planBenefitsSummary = objectMapper.convertValue(summary, new TypeReference<Map<String, Object>>() {});
				showBenefit1 = Boolean.valueOf(planBenefitsSummary.getOrDefault(DISPLAY_BENEFIT_1, Boolean.FALSE).toString());
				showBenefit2 = Boolean.valueOf(planBenefitsSummary.getOrDefault(DISPLAY_BENEFIT_2, Boolean.FALSE).toString());
				showBenefit3 = Boolean.valueOf(planBenefitsSummary.getOrDefault(DISPLAY_BENEFIT_3, Boolean.FALSE).toString());
				showBenefit4 = Boolean.valueOf(planBenefitsSummary.getOrDefault(DISPLAY_BENEFIT_4, Boolean.FALSE).toString());
				showBenefit5 = Boolean.valueOf(planBenefitsSummary.getOrDefault(DISPLAY_BENEFIT_5, Boolean.FALSE).toString());
				showBenefit6 = Boolean.valueOf(planBenefitsSummary.getOrDefault(DISPLAY_BENEFIT_6, Boolean.FALSE).toString());
			}
		}
	}


}
