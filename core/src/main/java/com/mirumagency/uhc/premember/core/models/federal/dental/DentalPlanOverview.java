package com.mirumagency.uhc.premember.core.models.federal.dental;

import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PremiumRates;
import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PlanCosts;
import com.mirumagency.uhc.premember.core.services.federal.DentalPremiumRateService;
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
import javax.servlet.http.Cookie;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.util.Asserts.notNull;

@Model( adaptables = SlingHttpServletRequest.class,
        adapters = DentalPlanOverview.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "premember/components/federal/content/dentalPlansOverview")
@Exporter(name = "jackson", extensions = "json")
public class DentalPlanOverview {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String DETAILS_PATH = "dental-plans/plan-details.%s";

    @Inject
    private Page currentPage;

    @Inject
    private PlansService plansService;

    @Inject
    private DentalPremiumRateService dentalPremiumRateService;

    @Self
    private SlingHttpServletRequest request;

    @Getter
    private List<DentalPlanDTO> dentalPlans;

    @JsonIgnore
    @Getter
    private String plansAsJson;


    @PostConstruct
    protected void init() {
        try {
            Cookie dentalRegionCodeCookie = request.getCookie("federal-dental-region-code");
            notNull(dentalRegionCodeCookie, "dentalRegionCodeCookie");
            String dentalRegionCode = dentalRegionCodeCookie.getValue();
            String pageUrl = request.getPathInfo();
            List<PremiumRates> premiumRates = dentalPremiumRateService.loadPremiumRates(dentalRegionCode);
            dentalPlans = plansService.loadPlan(pageUrl, dentalRegionCode)
                    .filter(plan -> plan.getType().equals(PlanType.DENTAL))
                    .map(Plan::getOptions)
                    .map(Collection::stream)
                    .map(planOptionStream ->
                            planOptionStream
                                    .map(planOption -> mapToPlanDto(planOption, premiumRates))
                                    .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            plansAsJson = JsonUtil.mapToJson(dentalPlans, true);
        } catch (JsonProcessingException e) {
            logger.error("Can't serialize {} to JSON", this, e);
            plansAsJson = StringUtils.EMPTY;
        } catch (Exception e) {
            logger.error("An error has occurred when trying to initialize Dental Plan Overview component.", e);
            plansAsJson = StringUtils.EMPTY;
        }
    }

    private DentalPlanDTO mapToPlanDto(PlanOption planOption, List<PremiumRates> premiumRates) {
        PremiumRates rate = premiumRates
                .stream()
                .filter(rates -> rates.getPlanType().equals(planOption.getId()))
                .findFirst()
                .orElse(null);
        return DentalPlanDTO.builder()
                .type(planOption.getId())
                .title(planOption.getName())
                .description(planOption.getDescription())
                .planCosts(PlanCostsMapper.map(planOption))
                .premiumRates(rate)
                .detailsLink(String.format(DETAILS_PATH, planOption.getId()))
                .build();
    }

    @JsonIgnore
    public boolean isEmpty(){
        return dentalPlans == null || dentalPlans.isEmpty();
    }

    @Builder
    @Getter
    @Setter
    public static class DentalPlanDTO {
        private String type;
        private String title;
        private String description;
        private String detailsLink;
        private PlanCosts planCosts;
        private PremiumRates premiumRates;
    }
}
