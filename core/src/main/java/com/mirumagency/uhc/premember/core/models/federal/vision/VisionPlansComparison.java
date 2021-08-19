package com.mirumagency.uhc.premember.core.models.federal.vision;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Model( adaptables = SlingHttpServletRequest.class,
        adapters = VisionPlansComparison.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class VisionPlansComparison {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String COPAYS_KEY = "copays";

    private static final String ALLOWANCES_KEY = "allowances";

    @Inject
    private PlansService plansService;

    @Self
    private SlingHttpServletRequest request;

    @Getter
    private String plansAsJson;

    @Getter
    private List<VisionPlanDTO> visionPlans;

    @Getter
    private Map<String, Object> labels = Collections.emptyMap();


    @PostConstruct
    protected void init() {
        try {
            String pageUrl = request.getPathInfo();
            Optional<Plan> plans = plansService.loadPlan(pageUrl).filter(plan -> plan.getType().equals(PlanType.VISION));
            labels = plans.get().getLabels();
            visionPlans = plans.map(plan -> plan.getOptions())
                    .map(Collection::stream)
                    .map(planOptionStream ->
                            planOptionStream
                                    .map(planOption -> mapToVisionPlan(planOption))
                                    .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            plansAsJson = JsonUtil.mapToJson(PlansDTO.builder()
                    .visionPlansList(visionPlans)
                    .labels(labels)
                    .build(), true);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", this, e);
            plansAsJson = StringUtils.EMPTY;
        }
    }

    private VisionPlanDTO mapToVisionPlan(PlanOption planOption) {
        return VisionPlanDTO.builder()
                .type(planOption.getId())
                .title(planOption.getName())
                .premiumRates(PremiumRatesMapper.map(planOption))
                .copays(mapBenefits(planOption.getData(), COPAYS_KEY))
                .allowances(mapBenefits(planOption.getData(), ALLOWANCES_KEY))
                .planBenefitsSummaryPdf(planOption.getPlanBenefitsSummaryPdf())
                .build();
    }

    private Map<String, Object> mapBenefits(Map<String, Object> data, String key) {
        Map<String, Object> benefitsMap = Collections.emptyMap();
        ObjectMapper objectMapper = new ObjectMapper();
        if(data != null && data.containsKey(key)){
            Object benefitObject = data.get(key);
            if (null != benefitObject) {
                benefitsMap = objectMapper.convertValue(benefitObject, new TypeReference<Map<String, Object>>() {});
            }
        }
        return benefitsMap;
    }

    public boolean isEmpty(){
        return visionPlans == null || visionPlans.isEmpty();
    }

    @Builder
    @Getter
    @Setter
    public static class PlansDTO {
        private Map<String, Object> labels;
        private List<VisionPlanDTO> visionPlansList;
    }

    @Builder
    @Getter
    @Setter
    public static class VisionPlanDTO {
        private String type;
        private String title;
        private PremiumRates premiumRates;
        private Map<String,Object> copays;
        private Map<String,Object> allowances;
        private String planBenefitsSummaryPdf;
    }
}
