package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import java.util.Map;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = PharmacyProviderList.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PharmacyProviderList extends TokensForEmployer {

    private Map<String, String> pharmacyPlanTokens;

    @PostConstruct
    protected void initModel() {
        Plan plan = getEmployer().getPlans().getPlanByType(PlanType.PHARMACY);
        pharmacyPlanTokens = plan.getTokens();
    }

    @Override
    public Map<String, String> getTokens() {
        return pharmacyPlanTokens;
    }
}
