package com.mirumagency.uhc.premember.core.domain.federal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter
public class PremiumRates {
    @Accessors(chain = true)
    @Setter
    private String planType;
    private final EnrollmentType selfOnly;
    private final EnrollmentType selfPlusOne;
    private final EnrollmentType selfAndFamily;

    @Builder
    @Getter
    public static class EnrollmentType {
        private final String enrollmentCode;
        private final NonPostalEmployee nonPostalEmployee;
        private final PostalEmployee postalEmployee;
    }

    @Builder
    @Getter
    public static class NonPostalEmployee {
        private final Double biweekly;
        private final Double monthly;
    }

    @Builder
    @Getter
    public static class PostalEmployee {
        private final Double biweeklyCategory1;
        private final Double biweeklyCategory2;
    }
}
