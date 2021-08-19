package com.mirumagency.uhc.premember.core.domain.provider;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.domain.plans.Plans;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Getter
public abstract class PlanList {

  List<ByPlan> providersByPlans;

  PlanList(Employer employer) {
    Plans plans = employer.getPlans();
    providersByPlans = getPlanTypesInOrder().stream()
            .map(plans::getPlanByType)
            .map(ByPlan::of)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toList());
  }

  abstract List<PlanType> getPlanTypesInOrder();

  public Map<String, String> getTokens() {
    String planList = getPlanList();
    if (planList.isEmpty()) {
      return ImmutableMap.of();
    }
    return ImmutableMap.of("planList", planList);
  }

  private String getPlanList() {
    String plansString = getPlanTypesInOrderForHero().stream()
            .filter(type -> providersByPlans.stream().anyMatch(provider -> provider.isOfType(type)))
            .map(PlanType::getTypeName)
            .collect(joining(", "));
    return replaceLastIn(plansString, ", ", " and ");
  }

  abstract List<PlanType> getPlanTypesInOrderForHero();

  private String replaceLastIn(String string, String toReplace, String replacement) {
    if (!string.contains(toReplace)) {
      return string;
    }
    int start = string.lastIndexOf(toReplace);
    return string.substring(0, start)
            + replacement
            + string.substring(start + toReplace.length());
  }
}
