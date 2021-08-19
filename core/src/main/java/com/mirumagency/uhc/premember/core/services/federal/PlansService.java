package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.domain.federal.Plans;
import com.mirumagency.uhc.premember.core.services.federal.repository.DentalPremiumRatesRepo;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlansRepo;
import com.mirumagency.uhc.premember.core.services.federal.repository.PremiumRatesRepo;

import static com.google.common.collect.ImmutableList.of;

import com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.PlanPath;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.http.util.Asserts.notNull;

import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.HEALTH;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.VISION;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.DENTAL;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.MEDICARE;

@Component(service = PlansService.class)
public class PlansService {
    private static final List<PlanType> PLAN_TYPES = of(HEALTH, VISION, DENTAL, MEDICARE);

    @Reference
    PlansRepo plansRepo;

    @Reference
    PremiumRatesRepo premiumRatesRepo;

    @Reference
    DentalPremiumRatesRepo dentalPremiumRatesRepo;

    public Optional<Plan> loadPlan(String planUrl) {
        notNull(planUrl, "planUrl");
        PlanLoader planLoader = new GeneralPlanLoader(plansRepo);
        return planLoader.loadPlan(planUrl);
    }

    public Optional<Plan> loadPlan(String planUrl, String regionCode) {
        notNull(planUrl, "planUrl");
        PlanLoader planLoader = new GeneralPlanLoader(plansRepo);
        PlanType planType = plansRepo.getPlanType(planUrl);
        if (planType.equals(PlanType.HEALTH)) {
            planLoader = new HealthPlanLoaderDecorator(planLoader, premiumRatesRepo);
        }
        if (planType.equals(PlanType.DENTAL)) {
            planLoader = new DentalPlanLoaderDecorator(planLoader, dentalPremiumRatesRepo);
        }
        return planLoader.loadPlan(planUrl, regionCode);
    }

    public String getPlanName(String planType, String planId) {
        return plansRepo.getPlanName(planType, planId);
    }

    public Plans loadPlans(String healthRegionCode, String dentalRegionCode) {
        List<Plan> plans = new ArrayList<>();
        PLAN_TYPES.forEach(planType -> {
            String regionCode = planType == HEALTH ? healthRegionCode : dentalRegionCode;
            Optional.of(planType)
                    .map(PlanPath::getPlanPage)
                    .map(planUrl -> loadPlan(planUrl, regionCode))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .ifPresent(plans::add);
        });
        return new Plans(plans);
    }
}
