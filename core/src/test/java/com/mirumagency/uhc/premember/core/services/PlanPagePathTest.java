package com.mirumagency.uhc.premember.core.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPagePath;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class PlanPagePathTest {

  private static final String EMPLOYER_ROOT_PATH = "/content/premember/employers/demo/basic-employer/en/home";

  @Test
  void shouldResolvePlanPagePathByType() {
    // given
    EmployerPath employerPath = EmployerPath.of(EMPLOYER_ROOT_PATH + "/anything_can_be_typed_here");
    // when
    // then
    Arrays.stream(PlanType.values()).forEach(planType ->
        assertEquals(EMPLOYER_ROOT_PATH + "/" + planType.getTypeName() + "-plans",
            PlanPagePath.getPlanPage(employerPath, planType)));
  }
}
