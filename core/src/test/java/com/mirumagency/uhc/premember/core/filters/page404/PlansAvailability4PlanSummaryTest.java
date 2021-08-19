package com.mirumagency.uhc.premember.core.filters.page404;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.domain.plans.Plans;

class PlansAvailability4PlanSummaryTest {


  @Test
  void shouldReturnOnlyOneOptionWithRedirect() {
    // given
    Plans plans = Plans.builder().list(newArrayList(createVisionPlan())).build();
    Employer employer = Mockito.mock(Employer.class);
    EmployerPath path = Mockito.mock(EmployerPath.class);
    Mockito.when(employer.getPlans()).thenReturn(plans);
    Mockito.when(employer.getPath()).thenReturn(path);
    Mockito.when(path.path()).thenReturn("/content/premember/employers/demo/basic-employer");
    PlansAvailability4PlanSummary underTest = new PlansAvailability4PlanSummary(
        "/content/premember/employers/demo/basic-employer/en/home/vision-plans",
        employer);
    //when
    long numberPlansAvailable = underTest.numberPlansAvailable();
    RedirectResult redirect = underTest.redirect();

    //then
    assertEquals(1, numberPlansAvailable);
    assertTrue(redirect.isRedirect());
    assertEquals(
        "/content/premember/employers/demo/basic-employer/en/home/vision-plans/plan-details.standard_836290857",
        redirect.getRedirectUrl());
  }

  @Test
  void shouldReturnOnlyOneOptionWithoutRedirect() {
    // given
    Plans plans = Plans.builder().list(newArrayList(createPharmacyPlan())).build();
    Employer employer = Mockito.mock(Employer.class);
    Mockito.when(employer.getPlans()).thenReturn(plans);
    PlansAvailability4PlanSummary underTest = new PlansAvailability4PlanSummary(
        "/content/premember/employers/demo/basic-employer/en/home/pharmacy-plans",
        employer);
    //when
    long numberPlansAvailable = underTest.numberPlansAvailable();
    RedirectResult redirect = underTest.redirect();

    //then
    assertEquals(1, numberPlansAvailable);
    assertFalse(redirect.isRedirect());
  }

  @Test
  void shouldReturnMoreOptionsWithoutRedirect() {
    // given
    Plans plans = Plans.builder().list(newArrayList(createTwoVisionPlanOptions())).build();
    Employer employer = Mockito.mock(Employer.class);
    Mockito.when(employer.getPlans()).thenReturn(plans);
    PlansAvailability4PlanSummary underTest = new PlansAvailability4PlanSummary(
        "/content/premember/employers/demo/basic-employer/en/home/vision-plans",
        employer);
    //when
    long numberPlansAvailable = underTest.numberPlansAvailable();
    RedirectResult redirect = underTest.redirect();

    //then
    assertEquals(2, numberPlansAvailable);
    assertFalse(redirect.isRedirect());
  }

  private Plan createVisionPlan() {
    return Plan.builder().type(PlanType.VISION).options(newArrayList(
        PlanOption.builder().name("standard").variation("master").type(PlanType.VISION).build()))
        .build();
  }

  private Plan createPharmacyPlan() {
    return Plan.builder().type(PlanType.PHARMACY).options(newArrayList(
        PlanOption.builder().name("standard").variation("master").type(PlanType.PHARMACY).build()))
        .build();
  }

  private Plan createTwoVisionPlanOptions() {
    return Plan.builder().type(PlanType.VISION).options(newArrayList(
        PlanOption.builder().name("standard").variation("master").type(PlanType.VISION).build(),
        PlanOption.builder().name("enhanced").variation("master").type(PlanType.VISION).build()))
        .build();
  }
}
