package com.mirumagency.uhc.premember.core.models.federal.vision;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.models.federal.vision.dto.Copays;
import com.mirumagency.uhc.premember.core.models.federal.vision.dto.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import com.mirumagency.uhc.premember.core.util.JsonUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Model( adaptables = SlingHttpServletRequest.class,
        adapters = VisionPlanOverview.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VisionPlanOverview {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DETAILS_PATH = "vision-plans/plan-details.%s";

    @Inject
    private PlansService plansService;

    @Getter
    private List<VisionPlanDto> visionPlans;

    @Getter
    private String plansAsJson;

    @Self
    private SlingHttpServletRequest request;

    @PostConstruct
    protected void init() {
        try {
            String pageUrl = request.getPathInfo();
            visionPlans = plansService.loadPlan(pageUrl)
                .filter(plan -> plan.getType().equals(PlanType.VISION))
                .map(Plan::getOptions)
                .map(Collection::stream)
                .map(planOptionStream ->
                        planOptionStream
                            .map(planOption -> mapToVisionPlanDto(planOption))
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());;
            plansAsJson = JsonUtil.mapToJson(visionPlans, true);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", this, e);
            plansAsJson = StringUtils.EMPTY;
        }
    }

    private VisionPlanDto mapToVisionPlanDto(PlanOption planOption) {
        return VisionPlanDto.builder()
                .type(planOption.getId())
                .title(planOption.getName())
                .description(planOption.getDescription())
                .detailsLink(String.format(DETAILS_PATH, planOption.getId()))
                .copays(CopaysMapper.map(planOption))
                .premiumRates(PremiumRatesMapper.map(planOption))
                .build();
    }

    public boolean isEmpty(){
        return visionPlans == null || visionPlans.isEmpty();
    }

    @Builder
    @Getter
    @Setter
    public static class VisionPlanDto {
        private String type;
        private String title;
        private String description;
        private String detailsLink;
        private Copays copays;
        private PremiumRates premiumRates;
    }
}
