package com.mirumagency.uhc.premember.core.domain.plans;

import static com.google.common.collect.ImmutableList.of;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.DENTAL;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.FINANCIAL;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.FSA;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.HEALTH;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.PHARMACY;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.VISION;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Plans {

  private static final List<PlanType> MAIN_PLANS = of(HEALTH, PHARMACY, DENTAL, VISION, FSA,
      FINANCIAL);

  private final List<Plan> list;

  public static Plans empty() {
    return new Plans(of());
  }

  public List<PlanOption> getPlanOptionsByType(PlanType planType) {
    return list.stream()
        .filter(plan -> planType == plan.getType())
        .map(Plan::getOptions)
        .findFirst()
        .orElse(Collections.emptyList());
  }

  public Plan getPlanByType(PlanType type) {
    return list.stream()
        .filter(plan -> plan.getType().equals(type))
        .findFirst().orElseGet(() -> Plan.empty(type));
  }

  public Map<String, String> getTokens() {
    return ImmutableMap.<String, String>builder()
        .putAll(getTokensWithOptionValue())
        .build();
  }

  public Map<String, Integer> getTokensWithCount() {
    return calculatePlanCountTokens()
        .collect(toMap(
            PlansCountToken::getTokenName,
            PlansCountToken::getCount
        ));
  }

  Map<String, String> getTokensWithOptionValue() {
    return calculatePlanCountTokens()
        .collect(toMap(
            PlansCountToken::getTokenName,
            PlansCountToken::getOptionsTokenValue
        ));
  }

  public int countByType(PlanType planType) {
    return getPlanOptionsByType(planType).size();
  }

  private Stream<PlansCountToken> calculatePlanCountTokens() {
    return stream(PlanType.values())
        .map(type -> new PlansCountToken(type, countByType(type)));
  }

  public PlanOption getFirstOptionByType(PlanType planType) {
    return getPlanByType(planType).getFirstOption();
  }

  public List<Plan> getMainPlans() {
    return MAIN_PLANS.stream()
        .map(this::getPlanByType)
        .filter(Plan::notEmpty)
        .collect(toList());
  }
}
