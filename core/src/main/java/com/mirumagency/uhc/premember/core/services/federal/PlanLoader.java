package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.domain.federal.Plan;

import java.util.Optional;

public interface PlanLoader {

     Optional<Plan> loadPlan(String planDetailsUrl);
     Optional<Plan> loadPlan(String planDetailsUrl, String regionCode);
}
