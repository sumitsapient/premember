package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlanPageTest {

  @Test
  void isPlanComparisonTablePage() {
    assertTrue(PlanPage.isPlanComparisonTablePage("/content/premember/employers/demo/basic-employer/en/home/vision-plans/comparison-table"));
    assertTrue(PlanPage.isPlanComparisonTablePage("/content/premember/employers/demo/basic-employer/en/home/dental-plans/comparison-table"));
    assertTrue(PlanPage.isPlanComparisonTablePage("/content/premember/employers/demo/basic-employer/en/home/health-plans/comparison-table"));
    assertFalse(PlanPage.isPlanComparisonTablePage("/content/premember/employers/demo/basic-employer/en/home/health-plans/plan-details"));
  }

  @Test
  void isPlanSummaryPage() {
    assertTrue(PlanPage.isPlanSummaryPage("/content/premember/employers/demo/basic-employer/en/home/vision-plans"));
    assertTrue(PlanPage.isPlanSummaryPage("/content/premember/employers/demo/basic-employer/en/home/dental-plans"));
    assertTrue(PlanPage.isPlanSummaryPage("/content/premember/employers/demo/basic-employer/en/home/health-plans"));
    assertTrue(PlanPage.isPlanSummaryPage("/content/premember/employers/demo/basic-employer/en/home/financial-plans"));
    assertTrue(PlanPage.isPlanSummaryPage("/content/premember/employers/demo/basic-employer/en/home/fsa-plans"));
    assertTrue(PlanPage.isPlanSummaryPage("/content/premember/employers/demo/basic-employer/en/home/pharmacy-plans"));
    assertTrue(PlanPage.isPlanSummaryPage("/content/premember/employers/demo/basic-employer/en/home/search-for-a-prescription"));
    assertFalse(PlanPage.isPlanSummaryPage("/content/premember/employers/demo/basic-employer/en/home/dental-plans/plan-details"));
  }

  @Test
  void isPlanDetailsPage() {
    assertTrue(PlanPage.isPlanDetailsPage("/content/premember/employers/demo/basic-employer/en/home/dental-plans/plan-details"));
    assertTrue(PlanPage.isPlanDetailsPage("/content/premember/employers/demo/basic-employer/en/home/health-plans/plan-details"));
    assertTrue(PlanPage.isPlanDetailsPage("/content/premember/employers/demo/basic-employer/en/home/vision-plans/plan-details"));
    assertFalse(PlanPage.isPlanDetailsPage("/content/premember/employers/demo/basic-employer/en/home/vision-plans"));
  }

  @Test
  void isPlanBenefitsPage() {
    assertTrue(PlanPage.isPlanBenefitsPage("/content/premember/employers/demo/basic-employer/en/home/benefits"));
    assertTrue(PlanPage.isPlanBenefitsPage("/content/premember/employers/demo/corner-case-employer/en/home/benefits"));
    assertTrue(PlanPage.isPlanBenefitsPage("/content/premember/employers/demo/mirum/en/home/benefits"));
    assertFalse(PlanPage.isPlanBenefitsPage("/content/premember/employers/demo/business-employer/en/home/vision-plans"));
  }

  @Test
  void isSearchForPrescriptionPage() {
    assertTrue(PlanPage.isSearchForPrescriptionPage("/content/premember/employers/demo/basic-employer/en/home/search-for-a-prescription"));
    assertTrue(PlanPage.isSearchForPrescriptionPage("/content/premember/employers/demo/corner-case-employer/en/home/search-for-a-prescription"));
    assertTrue(PlanPage.isSearchForPrescriptionPage("/content/premember/employers/demo/mirum/en/home/search-for-a-prescription"));
    assertFalse(PlanPage.isSearchForPrescriptionPage("/content/premember/employers/demo/business-employer/en/home/dental-plans"));
  }

  @Test
  void isSearchForProvider() {
    assertTrue(PlanPage.isSearchForProviderPage("/content/premember/employers/demo/basic-employer/en/home/search-for-a-provider"));
    assertTrue(PlanPage.isSearchForProviderPage("/content/premember/employers/demo/corner-case-employer/en/home/search-for-a-provider"));
    assertTrue(PlanPage.isSearchForProviderPage("/content/premember/employers/demo/mirum/en/home/search-for-a-provider"));
    assertFalse(PlanPage.isSearchForProviderPage("/content/premember/employers/demo/business-employer/en/home/dental-plans"));
  }
}