package com.mirumagency.uhc.premember.core.domain.plans;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PlansTest {

  @Test
  void shouldReturnZerosForTokenCounts_whenPlansEmpty() {
    // given
    List<Plan> planList = Collections.emptyList();
    Plans plans = new Plans(planList);
    // when
    Map<String, Integer> tokensWithCount = plans.getTokensWithCount();
    // then
    assertAll(() -> {
      assertEquals(0, tokensWithCount.get("healthPlansCount"));
      assertEquals(0, tokensWithCount.get("visionPlansCount"));
      assertEquals(0, tokensWithCount.get("dentalPlansCount"));
      assertEquals(0, tokensWithCount.get("fsaPlansCount"));
      assertEquals(0, tokensWithCount.get("pharmacyPlansCount"));
      assertEquals(0, tokensWithCount.get("financialPlansCount"));
    });
  }

  @Test
  void shouldReturnCountValueGreaterThanZero_whenPlansNotEmpty() {
    // given
    Plan healthPlan = Plan.builder().type(PlanType.HEALTH).options(newArrayList(
        PlanOption.builder().name("catalyst").build(),
        PlanOption.builder().name("charter").build())).build();

    Plan visionPlan = Plan.builder().type(PlanType.VISION)
        .options(newArrayList(PlanOption.builder().build())).build();

    Plan fsaPlan = Plan.builder().type(PlanType.FSA)
        .options(newArrayList(PlanOption.builder().build())).build();
    List<Plan> planList = Lists.newArrayList(healthPlan, visionPlan, fsaPlan);
    Plans plans = new Plans(planList);
    // when
    Map<String, Integer> tokensWithCount = plans.getTokensWithCount();
    // then
    assertAll(() -> {
      assertEquals(2, tokensWithCount.get("healthPlansCount"));
      assertEquals(1, tokensWithCount.get("visionPlansCount"));
      assertEquals(0, tokensWithCount.get("dentalPlansCount"));
      assertEquals(1, tokensWithCount.get("fsaPlansCount"));
      assertEquals(0, tokensWithCount.get("pharmacyPlansCount"));
      assertEquals(0, tokensWithCount.get("financialPlansCount"));
    });
  }

  @Test
  void shouldReturnOptionsWithZerosForTokens_whenPlansEmpty() {
    // given
    List<Plan> planList = Collections.emptyList();
    Plans plans = new Plans(planList);
    // when
    Map<String, String> tokensWithCount = plans.getTokensWithOptionValue();
    // then
    assertAll(() -> {
      assertEquals("0 options", tokensWithCount.get("healthPlansCount"));
      assertEquals("0 options", tokensWithCount.get("visionPlansCount"));
      assertEquals("0 options", tokensWithCount.get("dentalPlansCount"));
      assertEquals("0 options", tokensWithCount.get("fsaPlansCount"));
      assertEquals("0 options", tokensWithCount.get("pharmacyPlansCount"));
      assertEquals("0 options", tokensWithCount.get("financialPlansCount"));
    });
  }

  @Test
  void shouldReturnOptionValuesGreaterThanZero_whenPlansNotEmpty() {
    // given
    Plan healthPlan = Plan.builder().type(PlanType.HEALTH).options(newArrayList(
        PlanOption.builder().name("catalyst").build(),
        PlanOption.builder().name("charter").build())).build();
    Plan visionPlan = Plan.builder().type(PlanType.VISION)
        .options(newArrayList(PlanOption.builder().build())).build();
    Plan fsaPlan = Plan.builder().type(PlanType.FSA)
        .options(newArrayList(PlanOption.builder().build())).build();

    List<Plan> planList = Lists.newArrayList(healthPlan, visionPlan, fsaPlan);
    Plans plans = new Plans(planList);
    // when
    Map<String, String> tokensWithCount = plans.getTokensWithOptionValue();
    // then
    assertAll(() -> {
      assertEquals("2 options", tokensWithCount.get("healthPlansCount"));
      assertEquals("1 option", tokensWithCount.get("visionPlansCount"));
      assertEquals("0 options", tokensWithCount.get("dentalPlansCount"));
      assertEquals("1 option", tokensWithCount.get("fsaPlansCount"));
      assertEquals("0 options", tokensWithCount.get("pharmacyPlansCount"));
      assertEquals("0 options", tokensWithCount.get("financialPlansCount"));
    });
  }
}