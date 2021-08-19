package com.mirumagency.uhc.premember.core.services.federal;

import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.federal.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.repository.PremiumRatesRepo;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Optional;

@Component(service = PremiumRateService.class)
public class PremiumRateService {

    @Reference
    PremiumRatesRepo premiumRatesRepo;

    public List<PremiumRates> loadPremiumRates(String regionCode){
        notNull(regionCode, "regionCode");
        return premiumRatesRepo.loadAll(regionCode);
    }

    public Optional<PremiumRates> load(String regionCode, String planId){
        notNull(regionCode, "regionCode");
        return premiumRatesRepo.load(regionCode, planId);
    }
}
