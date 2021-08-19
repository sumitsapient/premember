package com.mirumagency.uhc.premember.core.models.federal;

import com.mirumagency.uhc.premember.core.models.PrememberEmployerConfig;
import com.mirumagency.uhc.premember.core.models.plan.PlanWithUrls;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.Cookie;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = FederalEmployerConfig.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FederalEmployerConfig extends PrememberEmployerConfig {

    @Inject
    private PlansService plansService;

    @Getter
    private List<PlanWithUrls> mainPlans;

    @Self
    private SlingHttpServletRequest request;

    private String healthRegionCode = "";
    private String dentalRegionCode = "";

    @Override
    @PostConstruct
    protected void initModel() {
        loadUhcLogoPath();
        loadEmployerLogoPath();
        loadEmployerLogoDescription();
        Cookie healthRegionCookie = request.getCookie("federal-region-code");
        if (healthRegionCookie != null) {
            healthRegionCode = healthRegionCookie.getValue();
        }
        Cookie dentalRegionCookie = request.getCookie("federal-dental-region-code");
        if (dentalRegionCookie != null) {
            dentalRegionCode = dentalRegionCookie.getValue();
        }
        loadMainPlans();
    }

    @Override
    protected void loadMainPlans() {
        mainPlans = plansService.loadPlans(healthRegionCode, dentalRegionCode)
                .getPlansList()
                .stream()
                .filter(plan -> plan.getOptions() != null && !plan.getOptions().isEmpty())
                .map(plan -> FederalPlanWithUrls.of(getEmployer(), plan))
                .collect(toList());
    }

    @Override
    public boolean hideSearchForPrescription() {
        return true;
    }
}
