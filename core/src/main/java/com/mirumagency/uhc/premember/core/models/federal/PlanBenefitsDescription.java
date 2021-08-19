package com.mirumagency.uhc.premember.core.models.federal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = PlanBenefitsDescription.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = PlanBenefitsDescription.RESOURCE_TYPE)
public class PlanBenefitsDescription {
    protected static final String RESOURCE_TYPE = "premember/components/federal/content/plansBenefitsDescription";
    private static final String BENEFITS_DESCRIPTIONS_KEY = "benefitsDescriptions";

    @ValueMapValue
    private String planType;

    @ValueMapValue
    private String benefitKey;

    @Inject
    private PlansService plansService;

    @Self
    private SlingHttpServletRequest request;

    @Getter
    private String description;

    @PostConstruct
    private void initModel() {
        String pageUrl = request.getPathInfo();
        plansService.loadPlan(pageUrl).ifPresent(this::mapToModel);
    }

    private void mapToModel(com.mirumagency.uhc.premember.core.domain.federal.Plan plan) {
        String selectedOptionId = Optional.ofNullable(plan.getSelectedOption())
                .map(PlanOption::getId)
                .orElse(StringUtils.EMPTY);
        description = plan.getOptions()
                .stream()
                .filter(planOption -> planOption.getId().equals(selectedOptionId))
                .findFirst()
                .map(planOption -> convertToMap(planOption.getData()))
                .map(data -> data.get(BENEFITS_DESCRIPTIONS_KEY))
                .map(this::convertToMap)
                .map(benefitsDescriptions -> (String) benefitsDescriptions.get(benefitKey))
                .orElse(StringUtils.EMPTY);
    }

    private Map<String, Object> convertToMap(Object data) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(data, new TypeReference<Map<String, Object>>() {
        });
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(description);
    }
}