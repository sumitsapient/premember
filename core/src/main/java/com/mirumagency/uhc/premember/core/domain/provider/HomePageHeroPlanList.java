package com.mirumagency.uhc.premember.core.domain.provider;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import lombok.Getter;

import java.util.List;

@Getter
public class HomePageHeroPlanList extends PlanList {

  private HomePageHeroPlanList(Employer employer) {
    super(employer);
  }

  public static HomePageHeroPlanList of(Employer employer) {
    return new HomePageHeroPlanList(employer);
  }

  @Override
  List<PlanType> getPlanTypesInOrder() {
    return ImmutableList.of(
            PlanType.HEALTH,
            PlanType.DENTAL,
            PlanType.VISION,
            PlanType.BEHAVIORAL_HEALTH,
            PlanType.FINANCIAL
    );
  }

  @Override
  List<PlanType> getPlanTypesInOrderForHero() {
    return ImmutableList.of(
            PlanType.HEALTH,
            PlanType.DENTAL,
            PlanType.VISION,
            PlanType.FINANCIAL
    );
  }
}
