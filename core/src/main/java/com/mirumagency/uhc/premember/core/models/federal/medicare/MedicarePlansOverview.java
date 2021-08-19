package com.mirumagency.uhc.premember.core.models.federal.medicare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.models.federal.MultifieldMapper;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import com.mirumagency.uhc.premember.core.util.JsonUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
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
import java.util.stream.Collectors;

@Model( adaptables = SlingHttpServletRequest.class,
        adapters = MedicarePlansOverview.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "premember/components/federal/content/medicarePlansOverview")
@Exporter(name = "jackson", extensions = "json")
public class MedicarePlansOverview {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DETAILS_PATH = "medicare-plans/plan-details.%s";

    @Inject
    private PlansService plansService;

    @Getter
    private List<MedicarePlanDto> medicarePlans;

    @Getter
    private String plansAsJson;

    @Self
    private SlingHttpServletRequest request;

    @PostConstruct
    protected void init() {
        try {
            String pageUrl = request.getPathInfo();
            medicarePlans = plansService.loadPlan(pageUrl)
                .filter(plan -> plan.getType().equals(PlanType.MEDICARE))
                .map(Plan::getOptions)
                .map(Collection::stream)
                .map(planOptionStream ->
                        planOptionStream
                            .map(planOption -> mapToMedicarePlanDto(planOption))
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            plansAsJson = JsonUtil.mapToJson(medicarePlans, true);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", this, e);
            plansAsJson = StringUtils.EMPTY;
        }
    }

    private MedicarePlanDto mapToMedicarePlanDto(PlanOption planOption) {
        return MedicarePlanDto.builder()
                .type(planOption.getId())
                .title(planOption.getName())
                .description(planOption.getDescription())
                .detailsLink(String.format(DETAILS_PATH, planOption.getId()))
                .benefits(MultifieldMapper.map(planOption, "benefits"))
                .build();
    }

    public boolean isEmpty(){
        return medicarePlans == null || medicarePlans.isEmpty();
    }

    @Builder
    @Getter
    @Setter
    public static class MedicarePlanDto {
        private String type;
        private String title;
        private String description;
        private String detailsLink;
        private Map<String, Object> benefits;
    }
}
