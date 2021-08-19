package com.mirumagency.uhc.premember.core.filters.page404;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.domain.plans.Plans;

class PlansAvailability4PlanDetailsTest {

  @Test
  void shouldReturnZeroPlans4NotExistingOption() {
    // given
    Plans plans = Plans.builder().list(newArrayList(createVisionPlan())).build();
    Employer employer = Mockito.mock(Employer.class);
    Mockito.when(employer.getPlans()).thenReturn(plans);
    PlansAvailability4PlanDetails underTest = new PlansAvailability4PlanDetails(
        "/content/premember/employers/demo/basic-employer/en/home/vision-plans/plan-details.not-existing-option_836290857",
        employer);
    //when
    long result = underTest.numberPlansAvailable();
    RedirectResult redirect = underTest.redirect();

    //then
    assertEquals(0, result);
    assertFalse(redirect.isRedirect());
  }



  @Test
  void shouldReturnZeroForNotExistingVariation() {
    // given
    Plans plans = Plans.builder().list(newArrayList(createVisionPlan())).build();
    Employer employer = Mockito.mock(Employer.class);
    Mockito.when(employer.getPlans()).thenReturn(plans);
    PlansAvailability4PlanDetails underTest = new PlansAvailability4PlanDetails(
        "/content/premember/employers/demo/basic-employer/en/home/vision-plans/plan-details.standard_0000000",
        employer);
    //when
    long result = underTest.numberPlansAvailable();
    RedirectResult redirect = underTest.redirect();

    //then
    assertEquals(0, result);
    assertFalse(redirect.isRedirect());
  }

  @Test
  void shouldReturnOneForExistingOption() {
    // given
    Plans plans = Plans.builder().list(newArrayList(createVisionPlan())).build();
    Employer employer = Mockito.mock(Employer.class);
    Mockito.when(employer.getPlans()).thenReturn(plans);
    PlansAvailability4PlanDetails underTest = new PlansAvailability4PlanDetails(
        "/content/premember/employers/demo/basic-employer/en/home/vision-plans/plan-details.standard_836290857",
        employer);
    //when
    long result = underTest.numberPlansAvailable();
    RedirectResult redirect = underTest.redirect();

    //then
    assertEquals(1, result);
    assertFalse(redirect.isRedirect());
  }


  private Plan createVisionPlan() {
    return Plan.builder().type(PlanType.VISION).options(newArrayList(
        PlanOption.builder().name("standard").variation("master").type(PlanType.VISION).build()))
        .build();
  }
}
