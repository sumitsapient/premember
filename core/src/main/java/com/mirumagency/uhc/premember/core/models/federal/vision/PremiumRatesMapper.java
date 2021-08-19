package com.mirumagency.uhc.premember.core.models.federal.vision;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.models.federal.vision.dto.PremiumRates;

import java.util.Map;

public class PremiumRatesMapper {

    public static PremiumRates map(PlanOption planOption) {
        ObjectMapper oMapper = new ObjectMapper();
        Object rates = planOption.getData().get("premiumRates");
        Map<String, Object> premiumRates = oMapper.convertValue(rates, new TypeReference<Map<String, Object>>() {});

        Double selfOnlyBiweekly = Double.parseDouble(premiumRates.get("selfOnlyBiWeekly").toString());
        Double selfOnlyMonthly = Double.parseDouble(premiumRates.get("selfOnlyMonthly").toString());

        Double selfPlusOneBiweekly = Double.parseDouble(premiumRates.get("selfPlusOneBiWeekly").toString());
        Double selfPlusOneMonthly = Double.parseDouble(premiumRates.get("selfPlusOneMonthly").toString());

        Double selfPlusFamilyBiweekly = Double.parseDouble(premiumRates.get("selfPlusFamilyBiWeekly").toString());
        Double selfPlusFamilyMonthly = Double.parseDouble(premiumRates.get("selfPlusFamilyMonthly").toString());

        return PremiumRates
                .builder()
                .selfOnly(new PremiumRates.Rate(selfOnlyBiweekly, selfOnlyMonthly))
                .selfPlusOne(new PremiumRates.Rate(selfPlusOneBiweekly, selfPlusOneMonthly))
                .selfAndFamily(new PremiumRates.Rate(selfPlusFamilyBiweekly, selfPlusFamilyMonthly))
                .build();
    }
}
