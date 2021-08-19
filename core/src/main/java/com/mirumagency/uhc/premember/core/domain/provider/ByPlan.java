package com.mirumagency.uhc.premember.core.domain.provider;

import static java.util.stream.Collectors.toList;

import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ByPlan {

  private static final String PROVIDER_SEARCH_TAB_TITLE = "providerSearchTabTitle";
  private static final String PROVIDER_SEARCH_TAB_TEXT = "providerSearchTabText";

  final String tabTitle;
  final String tabText;
  final PlanType planType;
  final List<ByOption> byOptions;

  public static Optional<ByPlan> of(Plan plan) {
    if (plan.getOptions().isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(ByPlan.builder()
        .planType(plan.getType())
        .tabTitle(plan.getCopy().getOrDefault(PROVIDER_SEARCH_TAB_TITLE, ""))
        .tabText(plan.getCopy().getOrDefault(PROVIDER_SEARCH_TAB_TEXT, ""))
        .byOptions(mapOptions(plan))
        .build());
  }

  private static List<ByOption> mapOptions(Plan plan) {
    return plan.getOptions().stream()
        .map(option -> ByOption.of(option, plan.getCopy()))
        .collect(toList());
  }

  public boolean isOfType(PlanType type) {
    if (planType.equals(type)) {
      return true;
    }
    return planType.equals(PlanType.BEHAVIORAL_HEALTH) && type.equals(PlanType.HEALTH);
  }
}
