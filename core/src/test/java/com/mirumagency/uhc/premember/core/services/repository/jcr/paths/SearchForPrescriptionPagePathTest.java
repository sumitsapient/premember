package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import org.junit.jupiter.api.Test;

class SearchForPrescriptionPagePathTest {

  @Test
  public void shouldCheckIfSearchForPrescriptionPage() {
    assertTrue(SearchForPrescriptionPagePath.isSearchForPrescriptionPagePath("/content/premember/employer-blueprint/en/home/search-for-a-prescription"));
    assertTrue(SearchForPrescriptionPagePath.isSearchForPrescriptionPagePath("/content/premember/employers/cognifide/en/home/search-for-a-prescriptions"));
    assertTrue(SearchForPrescriptionPagePath.isSearchForPrescriptionPagePath("/content/premember/employers/demo/basic-employer/en/home/search-for-a-prescription"));
    assertFalse(SearchForPrescriptionPagePath.isSearchForPrescriptionPagePath("/content/premember/employer-blueprint/en/home/health-plans"));
    assertFalse(SearchForPrescriptionPagePath.isSearchForPrescriptionPagePath("/content/premember/employers/demo/basic-employer/en/home/benefits"));
  }

  @Test
  public void shouldExtractPlanType() {
    assertEquals(PlanType.PHARMACY, SearchForPrescriptionPagePath.of("/content/premember/employer-blueprint/en/home/search-for-a-prescription").extractPlanType());
    assertEquals(PlanType.PHARMACY, SearchForPrescriptionPagePath.of("/content/premember/employers/demo/basic-employer/en/home/search-for-a-prescription").extractPlanType());
  }
}