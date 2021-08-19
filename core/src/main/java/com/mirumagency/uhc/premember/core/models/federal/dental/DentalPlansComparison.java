package com.mirumagency.uhc.premember.core.models.federal.dental;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PlanCosts;
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
import javax.servlet.http.Cookie;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.http.util.Asserts.notNull;

@Model( adaptables = SlingHttpServletRequest.class,
        adapters = DentalPlansComparison.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DentalPlansComparison {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String PREVENTIVE_CARE_KEY = "preventiveCare";

    private static final String MEDICAL_CARE_KEY = "medicalCare";

    @Inject
    private PlansService plansService;

    @Self
    private SlingHttpServletRequest request;

    @Getter
    private String plansAsJson;

    @Getter
    private List<DentalPlanDTO> dentalPlans;

    @Getter
    private Map<String, Object> labels = Collections.emptyMap();


    @PostConstruct
    protected void init() {
        try {
            Cookie regionCodeCookie = request.getCookie("federal-dental-region-code");
            notNull(regionCodeCookie, "regionCodeCookie");
            String regionCode = regionCodeCookie.getValue();
            String pageUrl = request.getPathInfo();
            Optional<Plan> plans = plansService.loadPlan(pageUrl, regionCode).filter(plan -> plan.getType().equals(PlanType.DENTAL));
            labels = plans.get().getLabels();
            dentalPlans = plans.map(plan -> plan.getOptions())
                    .map(Collection::stream)
                    .map(planOptionStream ->
                            planOptionStream
                                    .map(planOption -> mapToDentalPlan(planOption))
                                    .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            plansAsJson = JsonUtil.mapToJson(PlansDTO.builder()
                    .dentalPlansList(dentalPlans)
                    .labels(labels)
                    .build(), true);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", this, e);
            plansAsJson = StringUtils.EMPTY;
        }
    }

    private DentalPlanDTO mapToDentalPlan(PlanOption planOption) {
        return DentalPlanDTO.builder()
                .type(planOption.getId())
                .title(planOption.getName())
                .planCosts(PlanCostsMapper.map(planOption))
                .preventiveCare(mapBenefits(planOption.getData(), PREVENTIVE_CARE_KEY))
                .medicalCare(mapBenefits(planOption.getData(), MEDICAL_CARE_KEY))
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
        return dentalPlans == null || dentalPlans.isEmpty();
    }

    @Builder
    @Getter
    @Setter
    public static class PlansDTO {
        private Map<String, Object> labels;
        private List<DentalPlanDTO> dentalPlansList;
    }

    @Builder
    @Getter
    @Setter
    public static class DentalPlanDTO {
        private String type;
        private String title;
        private PlanCosts planCosts;
        private Map<String,Object> preventiveCare;
        private Map<String,Object> medicalCare;
        private String planBenefitsSummaryPdf;
    }

}
