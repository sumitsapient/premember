package com.mirumagency.uhc.premember.core.domain.disclaimers;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOptionDetails;
import com.mirumagency.uhc.premember.core.domain.plans.PlanPage;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import java.util.List;
import org.junit.jupiter.api.Test;

class LegalDisclaimersTest {

  @Test
  public void shouldReturnNoLegalDisclaimers() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder().build();

    //when
    List<LegalDisclaimer> disclaimers = mappings
        .getDisclaimersFor(PlanOption.empty(PlanType.BEHAVIORAL_HEALTH));

    //then
    assertNotNull(disclaimers);
    assertTrue(disclaimers.isEmpty());
  }

  @Test
  public void shouldReturnNoLegalDisclaimersForHealthOption() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.VISION)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build()
        ))
        .build();

    PlanOption option = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of("copyProp", "disclaimer text")),
            ImmutableMap.of("optionProp", true)))
        .build();

    //when
    List<LegalDisclaimer> disclaimers = mappings
        .getDisclaimersFor(option);

    //then
    assertNotNull(disclaimers);
    assertTrue(disclaimers.isEmpty());
  }

  @Test
  public void shouldReturnNoLegalDisclaimersWhenOptionPropIsNotSelected() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.HEALTH)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build()
        ))
        .build();

    PlanOption option = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of("copyProp", "disclaimer text")),
            ImmutableMap.of("optionProp", false)))
        .build();

    //when
    List<LegalDisclaimer> disclaimers = mappings
        .getDisclaimersFor(option);

    //then
    assertNotNull(disclaimers);
    assertTrue(disclaimers.isEmpty());
  }

  @Test
  public void shouldReturnLegalDisclaimersWhenOptionPropIsSelected() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.HEALTH)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build()
        ))
        .build();

    PlanOption option = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of("copyProp", "disclaimer text")),
            ImmutableMap.of("optionProp", true)))
        .build();

    //when
    List<LegalDisclaimer> disclaimers = mappings
        .getDisclaimersFor(option);

    //then
    assertNotNull(disclaimers);
    assertEquals(1, disclaimers.size());
    assertEquals("disclaimer text", disclaimers.get(0).getText());
  }

  @Test
  public void shouldReturnTwoLegalDisclaimersWhenOptionPropIsSelected() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.HEALTH)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build(),
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.HEALTH)
                .planOptionPropertyName("optionProp2")
                .disclaimerTextCopyPropertyName("copyProp2")
                .build()
        ))
        .build();

    PlanOption option = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of("copyProp", "disclaimer text",
                "copyProp2", "disclaimer text 2")),
            ImmutableMap.of("optionProp", true,
                "optionProp2", true)))
        .build();

    //when
    List<LegalDisclaimer> disclaimers = mappings
        .getDisclaimersFor(option);

    //then
    assertNotNull(disclaimers);
    assertEquals(2, disclaimers.size());
    assertEquals("disclaimer text", disclaimers.get(0).getText());
    assertEquals("disclaimer text 2", disclaimers.get(1).getText());
  }

  @Test
  public void shouldHandleWhenThereIsNoCopy() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.HEALTH)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build()
        ))
        .build();

    PlanOption option = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of()),
            ImmutableMap.of("optionProp", true)))
        .build();

    //when
    List<LegalDisclaimer> disclaimers = mappings
        .getDisclaimersFor(option);

    //then
    assertNotNull(disclaimers);
    assertTrue(disclaimers.isEmpty());
  }

  @Test
  public void shouldHandleWhenThereIsNoOptionProp() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.HEALTH)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build()
        ))
        .build();

    PlanOption option = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of("copyProp", "disclaimer text")),
            ImmutableMap.of()))
        .build();

    //when
    List<LegalDisclaimer> disclaimers = mappings
        .getDisclaimersFor(option);

    //then
    assertNotNull(disclaimers);
    assertTrue(disclaimers.isEmpty());
  }

  @Test
  public void shouldIncludePlanOptionSpecificDisclaimers() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.HEALTH)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build()
        )).build();

    PlanOption option = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of("copyProp", "disclaimer text")),
            ImmutableMap.of("optionProp", true,
                "disclaimers", "This is plan option specific disclaimer.")))
        .build();
    List<LegalDisclaimer> disclaimers = mappings.getDisclaimersFor(option);
    option.withLegalDisclaimers(disclaimers);

    //when
    List<LegalDisclaimer> optionDisclaimers = option.getDisclaimers();

    //then
    assertNotNull(optionDisclaimers);
    assertEquals(2, optionDisclaimers.size());
    assertEquals(Type.LEGAL, optionDisclaimers.get(0).getType());
    assertEquals(of(PlanPage.DETAILS), optionDisclaimers.get(0).getVisibleOnPages());
    assertEquals("This is plan option specific disclaimer.", optionDisclaimers.get(0).getText());
    assertEquals(Type.LEGAL, optionDisclaimers.get(1).getType());
    assertEquals("disclaimer text", optionDisclaimers.get(1).getText());
  }

  @Test
  public void shouldIncludePlanOptionSpecificDisclaimersForPlanWithSummaryPageOnly() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.PHARMACY)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build()
        )).build();

    PlanOption option = PlanOption.builder()
        .type(PlanType.PHARMACY)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of("copyProp", "disclaimer text")),
            ImmutableMap.of("optionProp", true,
                "disclaimers", "This is plan option specific disclaimer.")))
        .build();
    List<LegalDisclaimer> disclaimers = mappings.getDisclaimersFor(option);
    option.withLegalDisclaimers(disclaimers);

    //when
    List<LegalDisclaimer> optionDisclaimers = option.getDisclaimers();

    //then
    assertNotNull(optionDisclaimers);
    assertEquals(2, optionDisclaimers.size());
    assertEquals(Type.LEGAL, optionDisclaimers.get(0).getType());
    assertEquals(of(PlanPage.SUMMARY), optionDisclaimers.get(0).getVisibleOnPages());
    assertEquals("This is plan option specific disclaimer.", optionDisclaimers.get(0).getText());
    assertEquals(Type.LEGAL, optionDisclaimers.get(1).getType());
    assertEquals("disclaimer text", optionDisclaimers.get(1).getText());
  }

  @Test
  public void shouldAggregateDisclaimersOnPlanLevel() {
    //given
    LegalDisclaimersMappings mappings = LegalDisclaimersMappings.builder()
        .mappings(of(
            LegalDisclaimerMapping.builder()
                .relatedType(PlanType.HEALTH)
                .planOptionPropertyName("optionProp")
                .disclaimerTextCopyPropertyName("copyProp")
                .build()
        )).build();

    PlanOption option1 = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            new PlanCopy(ImmutableMap.of("copyProp", "disclaimer text")),
            ImmutableMap.of("optionProp", true)))
        .build();

    PlanCopy copy = new PlanCopy(ImmutableMap.of());
    PlanOption option2 = PlanOption.builder()
        .type(PlanType.HEALTH)
        .details(new PlanOptionDetails(
            copy,
            ImmutableMap.of("disclaimers", "This is plan option specific disclaimer.")))
        .build();
    option1.withLegalDisclaimers(mappings.getDisclaimersFor(option1));
    option2.withLegalDisclaimers(mappings.getDisclaimersFor(option2));

    Plan plan = Plan.builder().options(of(option1, option2)).build();

    //when
    List<LegalDisclaimer> optionDisclaimers = plan.getDisclaimers();

    //then
    assertNotNull(optionDisclaimers);
    assertEquals(2, optionDisclaimers.size());
    assertEquals(Type.LEGAL, optionDisclaimers.get(0).getType());
    assertEquals("disclaimer text", optionDisclaimers.get(0).getText());
    assertEquals(Type.LEGAL, optionDisclaimers.get(1).getType());
    assertEquals("This is plan option specific disclaimer.", optionDisclaimers.get(1).getText());
  }

  @Test
  public void shouldShowComparisonDisclaimer() {
    //given
    Plan plan = Plan.builder()
        .copy(
            new PlanCopy(ImmutableMap.of("comparisonDisclaimer", "This is comparison disclaimer.")))
        .options(of())
        .build();

    //when
    List<LegalDisclaimer> optionDisclaimers = plan.getDisclaimers();

    //then
    assertNotNull(optionDisclaimers);
    assertEquals(1, optionDisclaimers.size());
    assertEquals(Type.LEGAL, optionDisclaimers.get(0).getType());
    assertEquals(of(PlanPage.COMPARISON_TABLE), optionDisclaimers.get(0).getVisibleOnPages());
    assertEquals("This is comparison disclaimer.", optionDisclaimers.get(0).getText());
  }
}