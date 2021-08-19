package com.mirumagency.uhc.premember.core.domain.plans;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    @Test
    void shouldGenerateTokensForEmptyHealthPlan() {
        // given
        Plan plan = Plan.builder().type(PlanType.HEALTH).options(ImmutableList.of()).build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertEquals("Demo health Plan Option", tokens.get("selectedOption.planName"));
    }

    @Test
    void shouldGenerateTokensForEmptyVisionPlan() {
        // given
        Plan plan = Plan.builder().type(PlanType.VISION).options(ImmutableList.of()).build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertEquals("Demo vision Plan Option", tokens.get("selectedOption.planName"));
    }

    @Test
    void shouldGenerateDataTokensForHealthPlan() {
        // given
        Plan plan = Plan.builder().type(PlanType.HEALTH).options(ImmutableList.of(healthOption())).build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertEquals("Catalyst", tokens.get("selectedOption.planName"));
        assertEquals("/content/dam/premember/documents/some.pdf", tokens.get("selectedOption.fullBenefitsLink"));
    }

    @Test
    void shouldGenerateCopyTokensForHealthPlan() {
        // given
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(ImmutableList.of(healthOption()))
                .copy(
                        new PlanCopy(ImmutableMap.of(
                                "summaryTopic1", "Network Coverage",
                                "summaryTopic1Option1", "Both network and out-of-network providers covered"
                        ))
                )
                .build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertEquals("Network Coverage", tokens.get("copy.summaryTopic1"));
        assertEquals("Both network and out-of-network providers covered", tokens.get("copy.summaryTopic1Option1"));
    }

    @Test
    void shouldReplaceOrBlankBooleansWithMatchingCopyText() {
        // given
        PlanCopy copy = new PlanCopy(ImmutableMap.of(
                "fsaOffered", "Flexible spending account (FSA) offered",
                "hsaOffered", "Health savings account (HSA) offered"
        ));
        ImmutableList<PlanOption> options = ImmutableList.of(healthOption(new PlanOptionDetails(
                copy, ImmutableMap.of(
                "fsaOffered", true,
                "hsaOffered", false
        )
        )));
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(options)
                .copy(copy)
                .build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertEquals("Flexible spending account (FSA) offered", tokens.get("selectedOption.fsaOffered.copy"));
        assertEquals("true", tokens.get("selectedOption.fsaOffered"));
        assertEquals("", tokens.get("selectedOption.hsaOffered.copy"));
        assertEquals("false", tokens.get("selectedOption.hsaOffered"));
    }

    @Test
    void shouldReplaceOrBlankBooleansFromEmployerConfigurationWithMatchingCopyText() {
        // given
        PlanCopy copy = new PlanCopy(ImmutableMap.of(
                "fsaOffered", "Flexible spending account (FSA) offered",
                "hsaOffered", "Health savings account (HSA) offered"
        ));
        ImmutableList<PlanOption> options = ImmutableList.of(healthOption(new PlanOptionDetails(
                copy, ImmutableMap.of(
                "fsaOffered", "true",
                "hsaOffered", "false"
        )
        )));
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(options)
                .copy(copy)
                .build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertEquals("Flexible spending account (FSA) offered", tokens.get("selectedOption.fsaOffered.copy"));
        assertEquals("true", tokens.get("selectedOption.fsaOffered"));
        assertEquals("", tokens.get("selectedOption.hsaOffered.copy"));
        assertEquals("false", tokens.get("selectedOption.hsaOffered"));
    }

    @Test
    void shouldNotReplaceBooleansWhenThereIsNoMatchingCopyText() {
        // given
        PlanCopy copy = new PlanCopy(ImmutableMap.of(
                "variableName", "Different value"
        ));
        ImmutableList<PlanOption> options = ImmutableList.of(healthOption(new PlanOptionDetails(
                copy, ImmutableMap.of(
                "fsaOffered", true,
                "hsaOffered", false
        )
        )));
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(options)
                .copy(copy)
                .build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertNull(tokens.get("selectedOption.fsaOffered.copy"));
        assertEquals("true", tokens.get("selectedOption.fsaOffered"));
        assertNull(tokens.get("selectedOption.hsaOffered.copy"));
        assertEquals("false", tokens.get("selectedOption.hsaOffered"));
        assertEquals("Different value", tokens.get("copy.variableName"));
        assertNull(tokens.get("selectedOption.variableName"));
    }

    @Test
    void shouldNotReplaceBooleansFromEmployerConfigurationWhenThereIsNoMatchingCopyText() {
        // given
        PlanCopy copy = new PlanCopy(ImmutableMap.of(
                "variableName", "Different value"
        ));
        ImmutableList<PlanOption> options = ImmutableList.of(healthOption(new PlanOptionDetails(
                copy, ImmutableMap.of(
                "fsaOffered", "true",
                "hsaOffered", "false"
        )
        )));
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(options)
                .copy(copy)
                .build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertNull(tokens.get("selectedOption.fsaOffered.copy"));
        assertEquals("true", tokens.get("selectedOption.fsaOffered"));
        assertNull(tokens.get("selectedOption.hsaOffered.copy"));
        assertEquals("false", tokens.get("selectedOption.hsaOffered"));
        assertEquals("Different value", tokens.get("copy.variableName"));
        assertNull(tokens.get("selectedOption.variableName"));
    }

    @Test
    void shouldSelectPlanOption() {
        // given
        ImmutableList<PlanOption> options = ImmutableList.of(healthOption("one"), healthOption("two"));
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(options)
                .selectedOptionId("two_836290857")
                .build();

        // when
        PlanOption selectedOption = plan.getSelectedOption();

        // then
        assertNotNull(selectedOption);
        assertEquals("two", selectedOption.getName());
    }

    @Test
    void shouldSelectDefaultPlanOption() {
        // given
        ImmutableList<PlanOption> options = ImmutableList.of(healthOption("one"), healthOption("two"));
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(options)
                .build();

        // when
        PlanOption selectedOption = plan.getSelectedOption();

        // then
        assertNotNull(selectedOption);
        assertEquals("one", selectedOption.getName());
    }

    @Test
    void shouldSelectDemoPlanOption() {
        // given
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(ImmutableList.of())
                .build();

        // when
        PlanOption selectedOption = plan.getSelectedOption();

        // then
        assertNotNull(selectedOption);
        assertEquals("demo-plan-option", selectedOption.getName());
    }


    @Test
    void shouldReplaceOrBlankTokensInPlanData() {
        // given
        PlanCopy copy = new PlanCopy(ImmutableMap.of(
                "titleFromCopy", "This is very magic title from copy!"
        ));
        ImmutableList<PlanOption> options = ImmutableList.of(healthOption(new PlanOptionDetails(
                copy, ImmutableMap.of(
                "title", "{titleFromCopy} - That would be title from copy!",
                "title2", "{nonExistingPlaceholder} - That should be empty, because there is no copy field to be resolved!"
        )
        )));
        Plan plan = Plan.builder()
                .type(PlanType.HEALTH)
                .options(options)
                .copy(copy)
                .build();

        // when
        Map<String, String> tokens = plan.getTokens();

        // then
        assertNotNull(tokens);
        assertEquals("This is very magic title from copy!", tokens.get("selectedOption.title.copy"));
        assertEquals("{titleFromCopy} - That would be title from copy!", tokens.get("selectedOption.title"));
        assertEquals("", tokens.get("selectedOption.title2.copy"));
        assertEquals("{nonExistingPlaceholder} - That should be empty, because there is no copy field to be resolved!", tokens.get("selectedOption.title2"));
    }

    private PlanOption healthOption() {
        return PlanOption.builder()
                .type(PlanType.HEALTH)
                .name("sample-option")
                .variation("master")
                .details(new PlanOptionDetails(PlanCopy.empty(), ImmutableMap.of(
                        "planName", "Catalyst",
                        "fullBenefitsLink", "/content/dam/premember/documents/some.pdf"
                )))
                .build();
    }

    private PlanOption healthOption(PlanOptionDetails details) {
        return PlanOption.builder()
                .type(PlanType.HEALTH)
                .name("sample-option-2")
                .variation("master")
                .details(details)
                .build();
    }

    private PlanOption healthOption(String name) {
        return PlanOption.builder()
                .type(PlanType.HEALTH)
                .name(name)
                .variation("master")
                .details(PlanOptionDetails.empty())
                .build();
    }
}
