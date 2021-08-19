package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.domain.federal.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.repository.PremiumRatesRepo;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class HealthPlanLoaderDecorator extends PlanLoaderDecorator {

    private final PremiumRatesRepo premiumRatesRepo;

    public HealthPlanLoaderDecorator(PlanLoader planLoader, PremiumRatesRepo premiumRatesRepo) {
        super(planLoader);
        this.premiumRatesRepo = premiumRatesRepo;
    }

    @Override
    public Optional<Plan> loadPlan(String planDetailsUrl) {
        return planLoader.loadPlan(planDetailsUrl);
    }

    @Override
    public Optional<Plan> loadPlan(String planDetailsUrl, String regionCode) {
        Optional<Plan> optionalPlan = loadPlan(planDetailsUrl);
        optionalPlan.ifPresent(plan -> filterNotAllowedPlanOptions(plan, regionCode));
        return optionalPlan;
    }

    private void filterNotAllowedPlanOptions(Plan plan, String regionCode) {
        List<PremiumRates> premiumRates = premiumRatesRepo.loadAll(regionCode);
        List<PlanOption> filteredOptions = plan
                .getOptions()
                .stream()
                .filter(option -> premiumRates
                        .stream()
                        .anyMatch(rate -> rate.getPlanType().equals(option.getId())))
                .collect(toList());
        plan.setOptions(filteredOptions);
    }
}
