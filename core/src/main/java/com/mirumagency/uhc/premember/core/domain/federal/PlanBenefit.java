package com.mirumagency.uhc.premember.core.domain.federal;

import org.apache.commons.lang.StringUtils;

import java.util.Optional;

import static java.util.Arrays.stream;

public enum PlanBenefit {

    MEDICAL("medical"),
    PRESCRIPTION("prescription"),
    DENTAL("dental"),
    VISION("vision"),
    WELLNESS("wellness");

    private final String benefitName;

    public String getBenefitName() {
        return this.benefitName;
    }

    PlanBenefit(String benefitName) {
        this.benefitName = benefitName;
    }

    public static Optional<PlanBenefit> from(String benefitName) {
        return stream(values())
                .filter(type -> StringUtils.equalsIgnoreCase(type.benefitName, benefitName))
                .findFirst();
    }
}
