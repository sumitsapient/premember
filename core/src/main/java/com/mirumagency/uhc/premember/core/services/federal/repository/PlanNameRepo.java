package com.mirumagency.uhc.premember.core.services.federal.repository;

import com.mirumagency.uhc.premember.core.domain.federal.PlanName;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;

import java.util.List;

public interface PlanNameRepo {
    List<PlanName> loadPlanTypes(PlanType planType);
}
