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
import java.util.Map;

import static com.google.common.collect.ImmutableList.of;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlanListTest {

  @Test
  public void shouldCreateEmptyCrossPlanSearchWhenThereAreNoPlans() {
    //given
    Employer employer = Employer.builder()
        .plans(new Plans(of()))
        .build();
    //when
    CrossPlanSearch crossPlanSearch = CrossPlanSearch.of(employer);
    //then
    assertTrue(crossPlanSearch.getProvidersByPlans().isEmpty());
  }

  @Test
  public void shouldCreateEmptyCrossPlanSearchWhenThereAreNoPlanOptions() {
    //given
    Employer employer = Employer.builder()
        .plans(new Plans(of(healthPlan(of()))))
        .build();
    //when
    CrossPlanSearch crossPlanSearch = CrossPlanSearch.of(employer);
    //then
    assertTrue(crossPlanSearch.getProvidersByPlans().isEmpty());
  }

  @Test
  public void shouldCreateHealthProviderForCatalyst() {
    //given
    PlanOption catalyst = catalyst();
    Plan health = healthPlan(of(catalyst));
    Employer employer = employer(of(health));
    //when
    CrossPlanSearch crossPlanSearch = CrossPlanSearch.of(employer);
    //then
    assertEquals(1, crossPlanSearch.getProvidersByPlans().size());

    ByPlan providersByPlan = crossPlanSearch.getProvidersByPlans().get(0);
    assertEquals("health Providers", providersByPlan.getTabTitle());
    assertEquals("Select a plan to see if your doctor...", providersByPlan.getTabText());
    assertEquals(1, providersByPlan.getByOptions().size());

    ByOption providerByOption = providersByPlan.getByOptions().get(0);
    assertEquals("Catalyst Plan", providerByOption.getPlanName());
    assertEquals("https://uhc.com", providerByOption.getProviderLink().getLink());
    assertEquals("Search the provider network", providerByOption.getProviderLink().getText());
    assertFalse(providerByOption.isHasStateSpecificProvider());
  }

  @Test
  public void shouldCreateHealthProviderForTwoOptions() {
    //given
    PlanOption catalyst = catalyst();
    PlanOption charterBalanced = charterBalanced();
    Plan health = healthPlan(of(catalyst, charterBalanced));
    Employer employer = employer(of(health));
    //when
    CrossPlanSearch crossPlanSearch = CrossPlanSearch.of(employer);
    //then
    assertEquals(1, crossPlanSearch.getProvidersByPlans().size());
    ByPlan healthProviders = crossPlanSearch.getProvidersByPlans().get(0);
    assertEquals("health Providers", healthProviders.getTabTitle());
    assertEquals("Select a plan to see if your doctor...", healthProviders.getTabText());

    assertEquals(2, healthProviders.getByOptions().size());
    ByOption catalystProviders = healthProviders.getByOptions().get(0);
    assertEquals("Catalyst Plan", catalystProviders.getPlanName());
    assertEquals("https://uhc.com", catalystProviders.getProviderLink().getLink());
    assertEquals("Search the provider network", catalystProviders.getProviderLink().getText());

    ByOption charterBalancedProviders = healthProviders.getByOptions().get(1);
    assertEquals("Charter Balanced Plan", charterBalancedProviders.getPlanName());
    assertEquals("https://uhc.com2", charterBalancedProviders.getProviderLink().getLink());
    assertEquals("Search the provider network",
        charterBalancedProviders.getProviderLink().getText());
  }

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

  @Test
  public void shouldCreateHealthProviderForBehavioralHealth() {
    //given
    PlanOption behavioralHealth = behavioralHealth();
    Plan health = plan(PlanType.BEHAVIORAL_HEALTH, of(behavioralHealth));
    Employer employer = employer(of(health));
    //when
    CrossPlanSearch crossPlanSearch = CrossPlanSearch.of(employer);
    //then
    assertEquals(1, crossPlanSearch.getProvidersByPlans().size());

    ByPlan providersByPlan = crossPlanSearch.getProvidersByPlans().get(0);
    assertEquals("behavioralhealth Providers", providersByPlan.getTabTitle());
    assertEquals("Select a plan to see if your doctor...", providersByPlan.getTabText());
    assertEquals(1, providersByPlan.getByOptions().size());

    ByOption providerByOption = providersByPlan.getByOptions().get(0);
    assertEquals("Behavioral Health Plan", providerByOption.getPlanName());
    assertEquals("https://uhc.com3", providerByOption.getProviderLink().getLink());
    assertEquals("Search the provider network", providerByOption.getProviderLink().getText());
  }

  @Test
  public void shouldAddStateSpecificProvider() {
    //given
    PlanOption optionWithStateSpecificProvider = PlanOption.builder()
        .details(new PlanOptionDetails(healthPlanCopy(), ImmutableMap.
            <String, Object>builder()
            .put("planName", "Some Plan")
            .put("providerSearchLink", "https://uhc.com")
            .put("hasStateSpecificProvider", true)
            .put("stateSpecificProviderDescription", "If you live in (applicable states)")
            .put("stateSpecificProviderLinkText", "Harvard Pilgrim Joint Ventures")
            .put("stateSpecificProviderLink", "http://somepage.com")
            .build()))
        .build();
    Plan health = healthPlan(of(optionWithStateSpecificProvider));
    Employer employer = employer(of(health));
    //when
    CrossPlanSearch crossPlanSearch = CrossPlanSearch.of(employer);
    //then
    ByPlan providersByPlan = crossPlanSearch.getProvidersByPlans().get(0);
    ByOption providerByOption = providersByPlan.getByOptions().get(0);
    assertEquals("Some Plan", providerByOption.getPlanName());

    assertTrue(providerByOption.isHasStateSpecificProvider());
    assertTrue(providerByOption instanceof ByOptionWithStateSpecificProvider);

    ByOptionWithStateSpecificProvider withStateSpecificProvider = (ByOptionWithStateSpecificProvider) providerByOption;
    assertNotNull(withStateSpecificProvider.getStateSpecificProvider());
    assertEquals("If you live in (applicable states)",
        withStateSpecificProvider.getStateSpecificProvider().getDescription());
    assertNotNull(withStateSpecificProvider.getStateSpecificProvider().getLink());
    assertEquals("Harvard Pilgrim Joint Ventures",
        withStateSpecificProvider.getStateSpecificProvider().getLink().getText());
    assertEquals("http://somepage.com",
        withStateSpecificProvider.getStateSpecificProvider().getLink().getLink());
  }

  @Test
  public void shouldSkipStateSpecificProvider() {
    //given
    PlanOption optionWithStateSpecificProvider = PlanOption.builder()
        .details(new PlanOptionDetails(healthPlanCopy(), ImmutableMap.
            <String, Object>builder()
            .put("planName", "Some Plan")
            .put("providerSearchLink", "https://uhc.com")
            .put("hasStateSpecificProvider", false)
            .put("stateSpecificProviderDescription", "If you live in (applicable states)")
            .put("stateSpecificProviderLinkText", "Harvard Pilgrim Joint Ventures")
            .put("stateSpecificProviderLink", "http://somepage.com")
            .build()))
        .build();
    Plan health = healthPlan(of(optionWithStateSpecificProvider));
    Employer employer = employer(of(health));
    //when
    CrossPlanSearch crossPlanSearch = CrossPlanSearch.of(employer);
    //then
    ByPlan providersByPlan = crossPlanSearch.getProvidersByPlans().get(0);
    ByOption providerByOption = providersByPlan.getByOptions().get(0);
    assertEquals("Some Plan", providerByOption.getPlanName());

    assertFalse(providerByOption.isHasStateSpecificProvider());
    assertFalse(providerByOption instanceof ByOptionWithStateSpecificProvider);
  }

  @Test
  void shouldCreateTokenWithListOfAllProviderPlans() {
    //given
    Employer employer = employer(of(
        plan(PlanType.BEHAVIORAL_HEALTH),
        plan(PlanType.DENTAL),
        plan(PlanType.VISION),
        plan(PlanType.HEALTH)
    ));

    //when
    Map<String, String> tokens = employer.getTokens(true);

    //then
    assertNotNull(tokens);
    assertTrue(tokens.containsKey("crossPlanProviderSearch.planList"));
    assertEquals("health, dental and vision",
        tokens.get("crossPlanProviderSearch.planList"));
  }

  @Test
  void shouldCreateTokenWithListOfSomeProviderPlans() {
    //given
    Employer employer = employer(of(
        plan(PlanType.BEHAVIORAL_HEALTH),
        plan(PlanType.DENTAL),
        plan(PlanType.HEALTH)
    ));

    //when
    Map<String, String> tokens = employer.getTokens(true);

    //then
    assertNotNull(tokens);
    assertTrue(tokens.containsKey("crossPlanProviderSearch.planList"));
    assertEquals("health and dental", tokens.get("crossPlanProviderSearch.planList"));
  }

  @Test
  void shouldCreateTokenWithListOfHealthProviderPlanWhenBehavioralHealthIsPresent() {
    //given
    Employer employer = employer(of(
        plan(PlanType.BEHAVIORAL_HEALTH),
        plan(PlanType.DENTAL)
    ));

    //when
    Map<String, String> tokens = employer.getTokens(true);

    //then
    assertNotNull(tokens);
    assertTrue(tokens.containsKey("crossPlanProviderSearch.planList"));
    assertEquals("health and dental", tokens.get("crossPlanProviderSearch.planList"));
  }

  @Test
  void shouldCreateTokenWithOnePlanEnrolled() {
    //given
    Employer employer = employer(of(
        plan(PlanType.DENTAL)
    ));

    //when
    Map<String, String> tokens = employer.getTokens(true);

    //then
    assertNotNull(tokens);
    assertTrue(tokens.containsKey("crossPlanProviderSearch.planList"));
    assertEquals("dental", tokens.get("crossPlanProviderSearch.planList"));
  }

  @Test
  void shouldNotCreateTokenWhenThereAreNoPlans() {
    //given
    Employer employer = employer(of());

    //when
    Map<String, String> tokens = employer.getTokens(true);

    //then
    assertNotNull(tokens);
    assertFalse(tokens.containsKey("crossPlanProviderSearch.planList"));
  }

  protected PlanOption behavioralHealth() {
    return PlanOption.builder()
        .details(new PlanOptionDetails(planCopy(PlanType.BEHAVIORAL_HEALTH), ImmutableMap.of(
            "planName", "Behavioral Health Plan",
            "providerSearchLink", "https://uhc.com3"
        )))
        .build();
  }

  protected Employer employer(List<Plan> plans) {
    return Employer.builder()
        .plans(new Plans(plans))
        .build();
  }

  protected Plan plan(PlanType type, List<PlanOption> options) {
    return Plan.builder().type(type)
        .copy(planCopy(type))
        .options(options)
        .build();
  }

  protected Plan plan(PlanType type) {
    return plan(type, of(PlanOption.empty(type)));
  }

  protected Plan healthPlan(List<PlanOption> options) {
    return Plan.builder()
        .copy(healthPlanCopy())
        .options(options)
        .type(PlanType.HEALTH)
        .build();
  }

  protected PlanOption charterBalanced() {
    return PlanOption.builder()
        .details(new PlanOptionDetails(healthPlanCopy(), ImmutableMap.of(
            "planName", "Charter Balanced Plan",
            "providerSearchLink", "https://uhc.com2"
        )))
        .build();
  }

  protected PlanCopy healthPlanCopy() {
    return planCopy(PlanType.HEALTH);
  }

  protected PlanCopy planCopy(PlanType type) {
    return new PlanCopy(ImmutableMap.of(
        "providerSearchTabTitle", type.getTypeName() + " Providers",
        "providerSearchTabText", "Select a plan to see if your doctor...",
        "summaryProviderSearchLinkText", "Search the provider network"
    ));
  }

  protected PlanOption catalyst() {
    return PlanOption.builder()
        .details(new PlanOptionDetails(healthPlanCopy(), ImmutableMap.of(
            "planName", "Catalyst Plan",
            "providerSearchLink", "https://uhc.com"
        )))
        .build();
  }
}