package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class EmployerPathTest {

    @Test
    void shouldRecognizeBlueprintPath() {
        // given
        // when
        EmployerPath underTest = EmployerPath.of("/content/premember/employer-blueprint/en/home/jcr:content/root/responsivegrid");

        // then
        assertEquals("/content/premember/employer-blueprint", underTest.path());
        assertTrue(underTest.isBlueprint());
        assertEquals("/content/premember/employer-blueprint/en/configuration/jcr:content/root/responsivegrid", underTest.configurationPath().path());
        assertEquals("/content/premember/employer-blueprint/en/configuration/jcr:content/root/responsivegrid/dentalPlans", underTest.plansListPath(PlanType.DENTAL).path());
    }

    @Test
    void shouldRecognizeEmployerPath() {
        // given
        // when
        EmployerPath underTest = EmployerPath.of("/content/premember/cognifide/en/home/jcr:content/root/responsivegrid");

        // then
        assertEquals("/content/premember/cognifide", underTest.path());
        assertFalse(underTest.isBlueprint());
        assertEquals("/content/premember/cognifide/en/configuration/jcr:content/root/responsivegrid", underTest.configurationPath().path());
        assertEquals("/content/premember/cognifide/en/configuration/jcr:content/root/responsivegrid/healthPlans", underTest.plansListPath(PlanType.HEALTH).path());
    }

    @Test
    void shouldRecognizeNestedEmployerPath() {
        // given
        // when
        EmployerPath underTest = EmployerPath.of("/content/premember/employers/demo/basic-employer/en/home");

        // then
        assertEquals("/content/premember/employers/demo/basic-employer", underTest.path());
        assertFalse(underTest.isBlueprint());
        assertEquals("/content/premember/employers/demo/basic-employer/en/configuration/jcr:content/root/responsivegrid", underTest.configurationPath().path());
        assertEquals("/content/premember/employers/demo/basic-employer/en/configuration/jcr:content/root/responsivegrid/healthPlans", underTest.plansListPath(PlanType.HEALTH).path());
    }

    @Test
    void shouldRejectNullPath() {
        // given
        try {
            // when
            EmployerPath.of(null);
            // then
            fail("Should throw exception");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    @Test
    void shouldFallBackToEmployerBlueprintPath() {
        // given
        // when
        EmployerPath underTest = EmployerPath.of("/content/we-retail/en/home/jcr:content/root/responsivegrid");

        // then
        assertEquals("/content/premember/employer-blueprint", underTest.path());
        assertTrue(underTest.isBlueprint());
        assertEquals("/content/premember/employer-blueprint/en/configuration/jcr:content/root/responsivegrid", underTest.configurationPath().path());
        assertEquals("/content/premember/employer-blueprint/en/configuration/jcr:content/root/responsivegrid/dentalPlans", underTest.plansListPath(PlanType.DENTAL).path());
    }

    @Test
    void shouldReplaceBlueprintPathsWithEmployerPath() {
        // given
        EmployerPath underTest = EmployerPath.of("/content/premember/employers/cognifide/en/home/jcr:content/root/responsivegrid");

        // when
        Map<String, Object> dataWithMappedBlueprintContentPaths = underTest
            .mapBlueprintUrls(ImmutableMap.<String, Object>builder()
                .put("checkbox", true)
                .put("text", "lorem ipsum")
                .put("url", "http://example.com")
                .put("blueprintPath",
                    "/content/premember/employer-blueprint/en/home/pharmacy-plans.html")
                .put("blueprintPath2",
                    "/content/premember/employer-blueprint/en/home/benefits.html#health_care_101-tab")
                .put("empty", "")
                .put("token", "{someToken}")
                .put("differentPath", "/content/dam/premember/icons/ic_hc.svg")
                .build()
            );

        // then
        assertEquals(true, dataWithMappedBlueprintContentPaths.get("checkbox"));
        assertEquals("lorem ipsum", dataWithMappedBlueprintContentPaths.get("text"));
        assertEquals("http://example.com", dataWithMappedBlueprintContentPaths.get("url"));
        assertEquals("/content/premember/employers/cognifide/en/home/pharmacy-plans.html", dataWithMappedBlueprintContentPaths.get("blueprintPath"));
        assertEquals("/content/premember/employers/cognifide/en/home/benefits.html#health_care_101-tab", dataWithMappedBlueprintContentPaths.get("blueprintPath2"));
        assertEquals("", dataWithMappedBlueprintContentPaths.get("empty"));
        assertEquals("{someToken}", dataWithMappedBlueprintContentPaths.get("token"));
        assertEquals("/content/dam/premember/icons/ic_hc.svg", dataWithMappedBlueprintContentPaths.get("differentPath"));
    }
}
