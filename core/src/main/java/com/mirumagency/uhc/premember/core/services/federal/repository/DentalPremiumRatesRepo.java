package com.mirumagency.uhc.premember.core.services.federal.repository;

import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PremiumRates;

import java.util.List;
import java.util.Optional;

public interface DentalPremiumRatesRepo {

	List<PremiumRates> loadAll(String regionCode);
	Optional<PremiumRates> load(String regionCode, String dentalPlanType);

}
