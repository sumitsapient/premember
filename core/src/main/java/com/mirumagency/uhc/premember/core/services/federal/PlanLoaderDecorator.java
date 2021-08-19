package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.domain.federal.Plan;

import java.util.Optional;

public class PlanLoaderDecorator implements PlanLoader {

    protected final PlanLoader planLoader;

    public PlanLoaderDecorator(PlanLoader planLoader) {
        this.planLoader = planLoader;
    }

    @Override
    public Optional<Plan> loadPlan(String planDetailsUrl) {
        return planLoader.loadPlan(planDetailsUrl);
    }

    @Override
    public Optional<Plan> loadPlan(String planDetailsUrl, String regionCode) {
        return loadPlan(planDetailsUrl);
    }
}
