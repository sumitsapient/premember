package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.models.plan.PlanWithUrls;
import com.mirumagency.uhc.premember.core.models.plan.PrememberPlanWithUrl;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = PrememberEmployerConfig.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PrememberEmployerConfig extends TokensForEmployer implements EmployerCommonConfig {

    private static final String HOME_PAGE_PATH_SUFFIX = "/en/home.html";

    private String uhcLogoPath;

    private String employerLogoPath;

    private String employerLogoDescription;

    private List<PlanWithUrls> mainPlans;

    @PostConstruct
    protected void initModel() {
        loadUhcLogoPath();
        loadEmployerLogoPath();
        loadMainPlans();
        loadEmployerLogoDescription();
    }

    protected void loadMainPlans() {
        mainPlans = getEmployer()
                .getPlans()
                .getMainPlans()
                .stream()
                .map(plan -> PrememberPlanWithUrl.of(getEmployer(), plan))
                .collect(toList());
    }

    protected void loadUhcLogoPath() {
        uhcLogoPath = getEmployer()
                .getUhcLogo()
                .getPath();
    }

    protected void loadEmployerLogoPath() {
        employerLogoPath = getEmployer()
                .getLogo()
                .getPath();
    }

    protected void loadEmployerLogoDescription() {
        employerLogoDescription = getEmployer()
                .getLogo()
                .getDescription();
    }

    public List<PlanWithUrls> getMainPlans() {
        return mainPlans;
    }

    public String getUhcLogoPath() {
        return uhcLogoPath;
    }

    public String homePagePath() {
        return getEmployer().getPath() + HOME_PAGE_PATH_SUFFIX;
    }

    public boolean isEmployerLogoAdded() {
        return StringUtils.isNotBlank(employerLogoPath);
    }

    public String getEmployerLogoPath() {
        return employerLogoPath;
    }

    public String getEmployerLogoDescription(){
        return employerLogoDescription;
    }

    public boolean hideSearchForPrescription() {
        return getEmployer().getPlans().getPlanOptionsByType(PlanType.PHARMACY).isEmpty();
    }
}
