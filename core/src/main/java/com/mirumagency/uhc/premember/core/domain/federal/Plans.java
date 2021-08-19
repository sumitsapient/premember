package com.mirumagency.uhc.premember.core.domain.federal;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.HEALTH;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.VISION;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.DENTAL;
import static com.mirumagency.uhc.premember.core.domain.federal.PlanType.MEDICARE;

@Getter
@Builder
@RequiredArgsConstructor
public class Plans {
    private static final List<PlanType> MAIN_PLANS = of(HEALTH, VISION, DENTAL, MEDICARE);

    private final List<Plan> plansList;

    public static Plans empty() {
        return new Plans(of());
    }
}
