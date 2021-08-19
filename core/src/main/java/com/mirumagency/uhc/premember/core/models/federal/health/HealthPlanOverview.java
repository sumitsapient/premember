package com.mirumagency.uhc.premember.core.models.federal.health;


import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.domain.federal.PremiumRates;
import com.mirumagency.uhc.premember.core.models.federal.health.dto.PlanCosts;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import com.mirumagency.uhc.premember.core.services.federal.PremiumRateService;
import com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.PlanPath;
import com.mirumagency.uhc.premember.core.util.JsonUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.util.Asserts.notNull;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = HealthPlanOverview.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = { "premember/components/federal/content/plansOverview",
                "premember/components/federal/content/healthPlansEnrollment" })
@Exporter(name = "jackson", extensions = "json")
public class HealthPlanOverview {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DETAILS_PATH = "health-plans/plan-details.%s";

    @Inject
    private Page currentPage;

    @Inject
    private PlansService plansService;

    @Inject
    private PremiumRateService premiumRateService;

    @Self
    private SlingHttpServletRequest request;

    @Getter
    private List<PlanDto> plans;

    @JsonIgnore
    @Getter
    private String plansAsJson;

    @RequestAttribute(name = "requestAsHealthPlansUrl")
    private boolean requestAsHealthPlansUrl;


    @PostConstruct
    protected void init() {
        try {
            Cookie regionCodeCookie = request.getCookie("federal-region-code");
            notNull(regionCodeCookie, "regionCodeCookie");
            String regionCode = regionCodeCookie.getValue();
            Cookie zip = request.getCookie("zip");
            notNull(zip,"zip");
            String zipCodeCookie = "." + zip.getValue();
            String pageUrl =  request.getPathInfo();
            if(requestAsHealthPlansUrl){
                pageUrl = getHealthPlansUrl();
            }
            List<PremiumRates> premiumRates = premiumRateService.loadPremiumRates(regionCode);
            plans = plansService.loadPlan(pageUrl, regionCode)
                    .filter(plan -> plan.getType().equals(PlanType.HEALTH))
                    .map(Plan::getOptions)
                    .map(Collection::stream)
                    .map(planOptionStream ->
                            planOptionStream
                                    .map(planOption -> mapToPlanDto(planOption, premiumRates, zipCodeCookie))
                                    .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            plansAsJson = JsonUtil.mapToJson(plans, true);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", this, e);
            plansAsJson = StringUtils.EMPTY;
        } catch (Exception e) {
            logger.error("An error has occurred when trying to initialize Health Plan Overview component", e);
            plansAsJson = StringUtils.EMPTY;
        }
    }

    private String getHealthPlansUrl(){
        return String.format(PlanPath.ROOT_PLAN_PATH, PlanType.HEALTH.getTypeName());
    }

    private PlanDto mapToPlanDto(PlanOption planOption, List<PremiumRates> premiumRates, String zipCodeCookie) {
        PremiumRates rate = premiumRates
                .stream()
                .filter(rates -> rates.getPlanType().equals(planOption.getId()))
                .findFirst()
                .orElse(null);
        return PlanDto.builder()
                .type(planOption.getId())
                .title(planOption.getName())
                .description(planOption.getDescription())
                .planCosts(PlanCostsMapper.map(planOption))
                .premiumRates(rate)
                .detailsLink(String.format(DETAILS_PATH + zipCodeCookie, planOption.getId()))
                .build();
    }

    @JsonIgnore
    public boolean isEmpty(){
        return plans == null || plans.isEmpty();
    }

    @Builder
    @Getter
    @Setter
    public static class PlanDto {
        private String type;
        private String title;
        private String description;
        private String detailsLink;
        private PlanCosts planCosts;
        private PremiumRates premiumRates;
    }
}
