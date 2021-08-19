package com.mirumagency.uhc.premember.core.services.federal.repository;

import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;

import java.util.Optional;

public interface PlansRepo {

    PlanType getPlanType(String pageUrl);

    Optional<Plan> loadPlan(String pageUrl);

    String getPlanName(String planType, String planId);
}
