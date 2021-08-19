package com.mirumagency.uhc.premember.core.models.plan;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PrememberPlanDetailsPage implements  PlanDetailsPage{
    private final String name;
    private final String url;

    public static PlanDetailsPage of(Employer employer, PlanOption option) {
        return builder()
                .name(option.getOrEmpty("planName"))
                .url(employer.getPath().path()
                        .concat("/en/home/")
                        .concat(option.getType().getTypeName())
                        .concat("-plans/plan-details.")
                        .concat(option.getId()))
                .build();
    }
}
