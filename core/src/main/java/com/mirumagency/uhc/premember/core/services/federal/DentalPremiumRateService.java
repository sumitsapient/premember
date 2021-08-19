package com.mirumagency.uhc.premember.core.services.federal;

import com.mirumagency.uhc.premember.core.models.federal.dental.dto.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.repository.DentalPremiumRatesRepo;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Optional;

import static org.apache.http.util.Asserts.notNull;

@Component(service = DentalPremiumRateService.class)
public class DentalPremiumRateService {

	@Reference
	DentalPremiumRatesRepo dentalPremiumRatesRepo;

	public List<PremiumRates> loadPremiumRates(String regionCode){
		notNull(regionCode, "regionCode");
		return dentalPremiumRatesRepo.loadAll(regionCode);
	}

	public Optional<PremiumRates> load(String regionCode, String planId){
		notNull(regionCode, "regionCode");
		return dentalPremiumRatesRepo.load(regionCode, planId);
	}
}
