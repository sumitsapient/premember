package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.testing.resourceresolver.MockValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlanOptionPathTest {

    private static final String FRAGMENT_PATH_PROP = "fragmentPath";
    private static final String VARIATION_NAME_PROP = "variationName";

    private static final String MOCK_FRAGMENT_PATH = "/content/dam/premember/plans/options/health/catalyst";
    private static final String MOCK_VARIATION_NAME = "mockVariationName";
    private static final String MOCK_HEALTH_PLAN_OPTION_PATH =
        MOCK_FRAGMENT_PATH + "/jcr:content/data/" + MOCK_VARIATION_NAME;

    private static final String EMPLOYER_PLAN_RESOURCE_TYPE = "premember/components/configuration/employerPlan";

    @Mock
    private Resource resource;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldExtractPlanTypeFromCorrectPlanPath() {
        // given
        // when
        PlanOptionPath underTest = new PlanOptionPath(MOCK_HEALTH_PLAN_OPTION_PATH);
        // then
        assertEquals(PlanType.HEALTH, underTest.extractPlanType());
    }

    @Test
    void shouldExtractPlanNameFromCorrectPlanPath() {
        // given
        // when
        PlanOptionPath underTest = new PlanOptionPath(MOCK_HEALTH_PLAN_OPTION_PATH);
        // then
        assertEquals("catalyst", underTest.extractPlanName());
    }

    @Test
    void shouldExtractVariationNameFromCorrectPlanPath() {
        // given
        // when
        PlanOptionPath underTest = new PlanOptionPath(MOCK_HEALTH_PLAN_OPTION_PATH);
        // then
        assertEquals(MOCK_VARIATION_NAME, underTest.extractPlanVariation());
    }

    @Test
    void shouldGetPlanOptionPath_whenValidPlanConfigIsGiven() {
        // given
        ImmutableMap<String, Object> planConfigProperties = ImmutableMap.of(
            FRAGMENT_PATH_PROP, MOCK_FRAGMENT_PATH,
            VARIATION_NAME_PROP, MOCK_VARIATION_NAME,
            "sling:resourceType", EMPLOYER_PLAN_RESOURCE_TYPE);
        ValueMap planConfigValueMap = new MockValueMap(resource, planConfigProperties);
        when(resource.getValueMap()).thenReturn(planConfigValueMap);

        NiceResource niceResource = NiceResource.of(resource);
        Optional<PlanOptionPath> planOptionPathOptional = PlanOptionPath.from(niceResource);
        // when
        PlanOptionPath underTest = planOptionPathOptional.get();
        // then
        assertAll(() -> {
            assertNotNull(underTest);
            assertEquals(MOCK_HEALTH_PLAN_OPTION_PATH, underTest.path());
        });
    }

    @Test
    void shouldThrowException_whenInvalidPlanConfigIsGiven() {
        // given
        ValueMap planConfigValueMap = new MockValueMap(resource, Collections.emptyMap());
        when(resource.getValueMap()).thenReturn(planConfigValueMap);

        NiceResource niceResource = NiceResource.of(resource);
        Optional<PlanOptionPath> planOptionPathOptional = PlanOptionPath.from(niceResource);
        // when
        // then
        assertThrows(NoSuchElementException.class, planOptionPathOptional::get);
    }
}