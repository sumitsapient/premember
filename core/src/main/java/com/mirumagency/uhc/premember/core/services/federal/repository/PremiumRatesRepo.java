package com.mirumagency.uhc.premember.core.services.federal.repository;

import com.mirumagency.uhc.premember.core.domain.federal.PremiumRates;

import java.util.List;
import java.util.Optional;

public interface PremiumRatesRepo {
    List<PremiumRates> loadAll(String regionCode);
    Optional<PremiumRates> load(String regionCode, String healthPlanType);
}
