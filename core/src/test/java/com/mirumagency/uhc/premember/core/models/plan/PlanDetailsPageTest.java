package com.mirumagency.uhc.premember.core.models.plan;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOptionDetails;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanDetailsPageTest {

    @Test
    public void shouldCreateHealthPlanDetailsPage(){
        //given
        Employer employer = Employer.builder()
                .path(EmployerPath.of("/content/premember/employers/demo/basic-employer/en/home"))
                .build();

        PlanOption option = PlanOption.builder()
                .type(PlanType.HEALTH)
                .details(new PlanOptionDetails(PlanCopy.empty(), ImmutableMap.of("planName", "Catalyst Plan")))
                .name("catalyst")
                .variation("master")
                .build();

        //when
        PlanDetailsPage detailsPage = PrememberPlanDetailsPage.of(employer, option);

        //then
        assertNotNull(detailsPage);
        assertEquals("Catalyst Plan", detailsPage.getName());
        assertEquals("/content/premember/employers/demo/basic-employer/en/home/health-plans/plan-details.catalyst_836290857", detailsPage.getUrl());
    }

    @Test
    public void shouldCreateVisionPlanDetailsPage(){
        //given
        Employer employer = Employer.builder()
                .path(EmployerPath.of("/content/premember/employers/demo/basic-employer/en/home"))
                .build();

        PlanOption option = PlanOption.builder()
                .type(PlanType.VISION)
                .details(new PlanOptionDetails(PlanCopy.empty(), ImmutableMap.of("planName", "Vision Plan")))
                .name("vision")
                .variation("master")
                .build();

        //when
        PlanDetailsPage detailsPage = PrememberPlanDetailsPage.of(employer, option);

        //then
        assertNotNull(detailsPage);
        assertEquals("Vision Plan", detailsPage.getName());
        assertEquals("/content/premember/employers/demo/basic-employer/en/home/vision-plans/plan-details.vision_836290857", detailsPage.getUrl());
    }

}