package com.mirumagency.uhc.premember.core.models.federal.health;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.models.federal.health.dto.PlanCosts;

import java.util.Map;

public class PlanCostsMapper {

    public static PlanCosts map(PlanOption planOption){
        ObjectMapper oMapper = new ObjectMapper();
        Object rates = planOption.getData().get("planCosts");
        Map<String, Double> ratesMap = oMapper.convertValue(rates, new TypeReference<Map<String, Double>>() {});
        PlanCosts.Cost deductible = new PlanCosts.Cost(
                ratesMap.get("deductibleIndividual"),
                ratesMap.get("deductibleFamily"));
        PlanCosts.Cost outOfPocketLimit = new PlanCosts.Cost(
                ratesMap.get("outOfPocketLimitIndividual"),
                ratesMap.get("outOfPocketLimitFamily"));
        return new PlanCosts(deductible, outOfPocketLimit);
    }

}
