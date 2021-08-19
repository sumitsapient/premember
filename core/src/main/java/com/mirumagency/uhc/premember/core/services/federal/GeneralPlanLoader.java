package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlansRepo;

import java.util.Optional;

import static org.apache.http.util.Asserts.notNull;

public class GeneralPlanLoader implements PlanLoader {

    private final PlansRepo plansRepo;

    public GeneralPlanLoader(PlansRepo plansRepo) {
        this.plansRepo = plansRepo;
    }

    public Optional<Plan> loadPlan(String planDetailsUrl) {
        notNull(planDetailsUrl, "planDetailsUrl");
        return plansRepo.loadPlan(planDetailsUrl);
    }

    public Optional<Plan> loadPlan(String planDetailsUrl, String regionCode) {
        notNull(planDetailsUrl, "planDetailsUrl");
        return loadPlan(planDetailsUrl);
    }
}
