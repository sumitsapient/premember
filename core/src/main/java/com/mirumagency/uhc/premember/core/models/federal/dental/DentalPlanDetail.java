package com.mirumagency.uhc.premember.core.models.federal.dental;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.models.federal.HeadingConfig;
import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PlanCosts;
import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.DentalPremiumRateService;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import com.mirumagency.uhc.premember.core.util.JsonUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import java.util.Optional;

import static org.apache.http.util.Asserts.notNull;

@Model(adaptables = SlingHttpServletRequest.class, adapters = DentalPlanDetail.class,
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DentalPlanDetail {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private PlansService plansService;

	@Self
	private SlingHttpServletRequest request;

	private PlanCosts planCosts;

	private PremiumRates premiumRates;

	private String planAsJson;

	private String regionCode;

	@Inject
	private DentalPremiumRateService dentalPremiumRateService;

	@Getter
	@Self
	private HeadingConfig headingConfig;

	@Getter
	@ValueMapValue
	private String title;

	@PostConstruct
	protected void init() {
		try {
			Cookie regionCodeCookie = request.getCookie("federal-dental-region-code");
			notNull(regionCodeCookie, "regionCodeCookie");
			regionCode = regionCodeCookie.getValue();
			String pageUrl = request.getPathInfo();

			Optional<Plan> plan = plansService.loadPlan(pageUrl, regionCode);
			plan.ifPresent(this::mapToModel);
		} catch (Exception e) {
			logger.error("An uncontrolled error has occurred when trying to get the dental plan details", e);
			planAsJson = StringUtils.EMPTY;
		}
	}

	private void mapToModel(Plan plan) {
		PlanOption selectedOption = plan.getSelectedOption();
		if (plan.getSelectedOption() == null) {
			logger.warn("No selected option for dental plan details was found");
			return;
		}
		planCosts = PlanCostsMapper.map(selectedOption);
		dentalPremiumRateService.load(regionCode, selectedOption.getId())
			.ifPresent(rates -> premiumRates = rates.setPlanType(null));
		try {
			planAsJson = JsonUtil.mapToJson(this, true);
		} catch (JsonProcessingException e) {
			logger.error("Can't serialize {} to JSON", this, e);
			planAsJson = StringUtils.EMPTY;
		}
	}

	public PlanCosts getPlanCosts() {
		return planCosts;
	}

	public PremiumRates getPremiumRates() {
		return premiumRates;
	}

	public String getPlanAsJson() {
		return planAsJson;
	}

	public boolean empty() {
		return planCosts == null || premiumRates == null;
	}
}
