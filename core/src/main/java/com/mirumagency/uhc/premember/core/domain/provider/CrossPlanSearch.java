package com.mirumagency.uhc.premember.core.domain.provider;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import lombok.Getter;

import java.util.List;

@Getter
public class CrossPlanSearch extends PlanList {

  private CrossPlanSearch(Employer employer) {
    super(employer);
  }

  public static CrossPlanSearch of(Employer employer) {
    return new CrossPlanSearch(employer);
  }

  @Override
  List<PlanType> getPlanTypesInOrder() {
    return ImmutableList.of(
            PlanType.HEALTH,
            PlanType.DENTAL,
            PlanType.VISION,
            PlanType.BEHAVIORAL_HEALTH
    );
  }

  @Override
  List<PlanType> getPlanTypesInOrderForHero() {
    return ImmutableList.of(
            PlanType.HEALTH,
            PlanType.DENTAL,
            PlanType.VISION
    );
  }
}
