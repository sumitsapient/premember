package com.mirumagency.uhc.premember.core.models.federal.health.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanCosts {
    private Cost deductible;
    private Cost outOfPocketLimit;

    public PlanCosts(Cost deductible, Cost outOfPocketLimit) {
        this.deductible = deductible;
        this.outOfPocketLimit = outOfPocketLimit;
    }

    public static PlanCosts empty() {
        return new PlanCosts(new Cost(0d, 0d), new PlanCosts.Cost(0d, 0d));
    }

    @Getter
    @Setter
    public static class Cost {
        public Cost(Double individual, Double family) {
            this.individual = individual;
            this.family = family;
        }
        private Double individual;
        private Double family;
    }
}
