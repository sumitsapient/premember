package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.junit.jupiter.api.Assertions.*;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import org.junit.jupiter.api.Test;

class PlanPagePathTest {

    @Test
    public void shouldCheckIfPlanPage() {
        assertTrue(PlanPagePath.isPlanResourcePath("/content/premember/employer-blueprint/en/home/vision-plans"));
        assertTrue(PlanPagePath.isPlanResourcePath("/content/premember/employers/cognifide/en/home/vision-plans"));
        assertTrue(PlanPagePath.isPlanResourcePath("/content/premember/employers/demo/basic-employer/en/home/vision-plans"));
        assertFalse(PlanPagePath.isPlanResourcePath("/content/premember/employer-blueprint/en/home/search-for-a-prescription"));
        assertFalse(PlanPagePath.isPlanResourcePath("/content/premember/employers/demo/basic-employer/en/home/benefits"));
    }

    @Test
    public void shouldExtractPlanType() {
        assertEquals(PlanType.VISION, PlanPagePath.of("/content/premember/employer-blueprint/en/home/vision-plans").getPlanType());
        assertEquals(PlanType.HEALTH, PlanPagePath.of("/content/premember/employer-blueprint/en/home/health-plans").getPlanType());
        assertEquals(PlanType.DENTAL, PlanPagePath.of("/content/premember/employer-blueprint/en/home/dental-plans").getPlanType());
        assertEquals(PlanType.HEALTH, PlanPagePath.of("/content/premember/employers/demo/basic-employer/en/home/health-plans").getPlanType());
        assertEquals(PlanType.FSA, PlanPagePath.of("/content/premember/employers/demo/basic-employer/en/home/fsa-plans").getPlanType());
    }

}
