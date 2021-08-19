package com.mirumagency.uhc.premember.core.models.federal;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.models.plan.PlanDetailsPage;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FederalPlanDetailsPage implements PlanDetailsPage {
    private final String name;
    private final String url;

    public static FederalPlanDetailsPage of(Employer employer, PlanOption option) {
        return builder()
                .name(option.getName())
                .url(employer.getPath().path()
                        .concat("/en/home/")
                        .concat(option.getType().getTypeName())
                        .concat("-plans/plan-details.")
                        .concat(option.getId()))
                .build();
    }
}


