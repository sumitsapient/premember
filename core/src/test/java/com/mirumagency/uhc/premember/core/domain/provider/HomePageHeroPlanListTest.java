package com.mirumagency.uhc.premember.core.domain.provider;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HomePageHeroPlanListTest extends PlanListTest {

  @Test
  public void shouldCreateHealthDentalVisionBehavioralProvidersInRightOrder() {
    //given
    List<Plan> plans = Arrays.stream(PlanType.values())
            .map(type -> plan(type, of(catalyst()))).collect(toList());
    Employer employer = employer(plans);

    //when
    HomePageHeroPlanList heroPlanList = HomePageHeroPlanList.of(employer);
    //then
    assertEquals(5, heroPlanList.getProvidersByPlans().size());
    ByPlan healthProviders = heroPlanList.getProvidersByPlans().get(0);
    assertEquals("health Providers", healthProviders.getTabTitle());

    ByPlan dentalProviders = heroPlanList.getProvidersByPlans().get(1);
    assertEquals("dental Providers", dentalProviders.getTabTitle());

    ByPlan visionProviders = heroPlanList.getProvidersByPlans().get(2);
    assertEquals("vision Providers", visionProviders.getTabTitle());

    ByPlan behavioralHealthProviders = heroPlanList.getProvidersByPlans().get(3);
    assertEquals("behavioralhealth Providers", behavioralHealthProviders.getTabTitle());
  }
}

