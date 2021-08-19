package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanDetailsPagePathTest {

    @Test
    public void shouldCheckIfPlanPage() {
        assertTrue(PlanDetailsPagePath.isPlanDetailsPagePath("/content/premember/employer-blueprint/en/home/vision-plans/plan-details.standard_master"));
        assertTrue(PlanDetailsPagePath.isPlanDetailsPagePath("/content/premember/employers/cognifide/en/home/vision-plans/plan-details.standard_master"));
        assertTrue(PlanDetailsPagePath.isPlanDetailsPagePath("/content/premember/employers/demo/basic-employer/en/home/vision-plans/plan-details.standard_master"));
        assertFalse(PlanDetailsPagePath.isPlanDetailsPagePath("/content/premember/employer-blueprint/en/home/search-for-a-prescription"));
        assertFalse(PlanDetailsPagePath.isPlanDetailsPagePath("/content/premember/employers/demo/basic-employer/en/home/benefits"));
    }

    @Test
    public void shouldExtractPlanType() {
        assertEquals(PlanType.VISION, new PlanDetailsPagePath("/content/premember/employer-blueprint/en/home/vision-plans/plan-details.standard_master").getPlanType());
        assertEquals(PlanType.HEALTH, new PlanDetailsPagePath("/content/premember/employer-blueprint/en/home/health-plans/plan-details.standard_master").getPlanType());
        assertEquals(PlanType.DENTAL, new PlanDetailsPagePath("/content/premember/employer-blueprint/en/home/dental-plans/plan-details.standard_master").getPlanType());
        assertEquals(PlanType.HEALTH, new PlanDetailsPagePath("/content/premember/employers/demo/basic-employer/en/home/health-plans/plan-details.standard_master").getPlanType());
    }

    @Test
    public void shouldExtractPlanOptionName() {
        assertEquals("standard", new PlanDetailsPagePath("/content/premember/employer-blueprint/en/home/vision-plans/plan-details.standard_master").getPlanOptionName());
        assertEquals("charter-balanced", new PlanDetailsPagePath("/content/premember/employer-blueprint/en/home/health-plans/plan-details.charter-balanced_master").getPlanOptionName());
        assertEquals("charter-balanced", new PlanDetailsPagePath("/content/premember/employers/demo/basic-employer/en/home/health-plans/plan-details.charter-balanced_master").getPlanOptionName());
        assertEquals("charter-balanced", new PlanDetailsPagePath("/content/premember/employers/demo/basic-employer/en/home/health-plans/plan-details.charter-balanced_master.html").getPlanOptionName());
    }

    @Test
    public void shouldExtractPlanOptionVariant() {
        assertEquals("836290857", new PlanDetailsPagePath("/content/premember/employer-blueprint/en/home/vision-plans/plan-details.standard_836290857").getPlanOptionHashedVariation());
        assertEquals("836290857", new PlanDetailsPagePath("/content/premember/employer-blueprint/en/home/health-plans/plan-details.charter-balanced_836290857").getPlanOptionHashedVariation());
        assertEquals("836290857", new PlanDetailsPagePath("/content/premember/employers/demo/basic-employer/en/home/health-plans/plan-details.charter-balanced_836290857").getPlanOptionHashedVariation());
        assertEquals("836290857", new PlanDetailsPagePath("/content/premember/employers/demo/basic-employer/en/home/health-plans/plan-details.charter-balanced_836290857.html").getPlanOptionHashedVariation());
    }
}
