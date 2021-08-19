package com.mirumagency.uhc.premember.core.domain.provider;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOptionDetails;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.domain.plans.Plans;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CrossPlanSearchTest extends PlanListTest {

  @Test
  public void shouldCreateHealthDentalVisionBehavioralProvidersInRightOrder() {
    //given
    List<Plan> plans = Arrays.stream(PlanType.values())
        .map(type -> plan(type, of(catalyst()))).collect(toList());
    Employer employer = employer(plans);

    //when
    CrossPlanSearch crossPlanSearch = CrossPlanSearch.of(employer);
    //then
    assertEquals(4, crossPlanSearch.getProvidersByPlans().size());
    ByPlan healthProviders = crossPlanSearch.getProvidersByPlans().get(0);
    assertEquals("health Providers", healthProviders.getTabTitle());

    ByPlan dentalProviders = crossPlanSearch.getProvidersByPlans().get(1);
    assertEquals("dental Providers", dentalProviders.getTabTitle());

    ByPlan visionProviders = crossPlanSearch.getProvidersByPlans().get(2);
    assertEquals("vision Providers", visionProviders.getTabTitle());

    ByPlan behavioralHealthProviders = crossPlanSearch.getProvidersByPlans().get(3);
    assertEquals("behavioralhealth Providers", behavioralHealthProviders.getTabTitle());
  }
}