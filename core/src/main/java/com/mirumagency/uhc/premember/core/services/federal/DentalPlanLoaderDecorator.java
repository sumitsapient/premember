package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.repository.DentalPremiumRatesRepo;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class DentalPlanLoaderDecorator extends PlanLoaderDecorator {

    private final DentalPremiumRatesRepo dentalPremiumRatesRepo;

    public DentalPlanLoaderDecorator(PlanLoader planLoader, DentalPremiumRatesRepo dentalPremiumRatesRepo) {
        super(planLoader);
        this.dentalPremiumRatesRepo = dentalPremiumRatesRepo;
    }

    @Override
    public Optional<Plan> loadPlan(String planDetailsUrl, String regionCode) {
        Optional<Plan> optionalPlan = loadPlan(planDetailsUrl);
        optionalPlan.ifPresent(plan -> filterNotAllowedPlanOptions(plan, regionCode));
        return optionalPlan;
    }

    private void filterNotAllowedPlanOptions(Plan plan, String regionCode) {
        List<PremiumRates> dentalPremiumRates = dentalPremiumRatesRepo.loadAll(regionCode);
        List<PlanOption> filteredOptions = plan
                .getOptions()
                .stream()
                .filter(option -> dentalPremiumRates
                        .stream()
                        .anyMatch(rate -> rate.getPlanType().equals(option.getId())))
                .collect(toList());
        plan.setOptions(filteredOptions);
    }
}
