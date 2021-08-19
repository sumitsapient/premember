package com.mirumagency.uhc.premember.core.models.federal.vision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.models.federal.HeadingConfig;
import com.mirumagency.uhc.premember.core.models.federal.vision.dto.Copays;
import com.mirumagency.uhc.premember.core.models.federal.vision.dto.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import com.mirumagency.uhc.premember.core.util.JsonUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = SlingHttpServletRequest.class, adapters = VisionPlanDetail.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VisionPlanDetail {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private PlansService plansService;

    @SlingObject
    private SlingHttpServletRequest request;

    @Getter
    private Copays copays;

    @Getter
    private PremiumRates premiumRates;

    @Getter
    private String planAsJson;

    @Getter
    @Self
    private HeadingConfig headingConfig;

    @Getter
    @ValueMapValue
    private String title;

    @PostConstruct
    protected void init() {
        String pageUrl = request.getPathInfo();
        plansService.loadPlan(pageUrl)
                .filter(plan -> plan.getType().equals(PlanType.VISION))
                .ifPresent(this::mapToModel);
    }

    private void mapToModel(Plan plan) {
        if (plan.getSelectedOption() == null) {
            logger.warn("No selected option for vision plan details was found");
            return;
        }
        copays = CopaysMapper.map(plan.getSelectedOption());
        premiumRates = PremiumRatesMapper.map(plan.getSelectedOption());
        convertModelToJson();
    }

    private void convertModelToJson() {
        try {
            planAsJson = JsonUtil.mapToJson(this, true);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", this, e);
            planAsJson = StringUtils.EMPTY;
        }
    }
    @JsonIgnore
    public boolean isEmpty(){
         return copays == null &&  premiumRates == null;
    }
}
