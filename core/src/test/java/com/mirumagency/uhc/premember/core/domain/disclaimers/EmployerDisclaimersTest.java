package com.mirumagency.uhc.premember.core.domain.disclaimers;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmployerDisclaimersTest {

  @Test
  public void shouldReturnNoDisclaimersForEmptyEmployer() {
    //given
    Employer employer = Employer.builder().build();
    Disclaimers disclaimers = Disclaimers.empty();

    //when
    EmployerDisclaimers employerDisclaimers = disclaimers.listFor(employer);

    //then
    assertNotNull(employerDisclaimers);
    assertTrue(employerDisclaimers.isEmpty());
  }

  @Test
  public void shouldReturnNoDisclaimersForValidEmployer() {
    //given
    Employer employer = Employer
        .builder()
        .state(State.ALL)
        .fundingMethod(FundingMethod.ASO)
        .build();
    Disclaimers disclaimers = Disclaimers.empty();

    //when
    EmployerDisclaimers employerDisclaimers = disclaimers.listFor(employer);

    //then
    assertNotNull(employerDisclaimers);
    assertTrue(employerDisclaimers.isEmpty());
  }

  @Test
  public void shouldReturnDisclaimersForValidEmployer() {
    //given
    Employer employer = Employer
        .builder()
        .state(State.ALL)
        .fundingMethod(FundingMethod.ASO)
        .build();
    Disclaimers disclaimers = Disclaimers.of(of(
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.FI)
            .state(State.ALL)
            .text("Lorem ipsum")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.ALL)
            .text("Lorem ipsum 2")
            .build()
    ));

    //when
    EmployerDisclaimers employerDisclaimers = disclaimers.listFor(employer);

    //then
    assertNotNull(employerDisclaimers);
    assertEquals(1, employerDisclaimers.size());
    assertEquals(FundingMethod.ASO, employerDisclaimers.get(0).getFundingMethod());
    assertEquals("Lorem ipsum 2", employerDisclaimers.get(0).getText());
  }

  @Test
  public void shouldReturnDisclaimersForAllFundingMethods() {
    //given
    Employer employer = Employer
        .builder()
        .state(State.ALL)
        .fundingMethod(FundingMethod.ASO)
        .build();
    Disclaimers disclaimers = Disclaimers.of(of(
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.FI)
            .state(State.ALL)
            .text("Lorem ipsum")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ALL)
            .state(State.ALL)
            .text("Lorem ipsum 2")
            .build()
    ));

    //when
    EmployerDisclaimers employerDisclaimers = disclaimers.listFor(employer);

    //then
    assertNotNull(employerDisclaimers);
    assertEquals(1, employerDisclaimers.size());
    assertEquals(FundingMethod.ALL, employerDisclaimers.get(0).getFundingMethod());
    assertEquals("Lorem ipsum 2", employerDisclaimers.get(0).getText());
  }

  @Test
  public void shouldReturnDisclaimersForNonNYState() {
    //given
    Employer employer = Employer
        .builder()
        .state(State.CA)
        .fundingMethod(FundingMethod.ASO)
        .build();
    Disclaimers disclaimers = Disclaimers.of(of(
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.CA)
            .text("Lorem ipsum")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.NY)
            .text("Lorem ipsum 2")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.ALL_EXCEPT_NY)
            .text("Lorem ipsum 2")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.ALL)
            .text("Lorem ipsum 2")
            .build()
    ));

    //when
    EmployerDisclaimers employerDisclaimers = disclaimers.listFor(employer);

    //then
    assertNotNull(employerDisclaimers);
    assertEquals(3, employerDisclaimers.size());
    assertEquals(State.CA, employerDisclaimers.get(0).getState());
    assertEquals(State.ALL_EXCEPT_NY, employerDisclaimers.get(1).getState());
    assertEquals(State.ALL, employerDisclaimers.get(2).getState());
  }

  @Test
  public void shouldReturnDisclaimersForNYState() {
    //given
    Employer employer = Employer
        .builder()
        .state(State.NY)
        .fundingMethod(FundingMethod.ASO)
        .build();
    Disclaimers disclaimers = Disclaimers.of(of(
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.CA)
            .text("Lorem ipsum")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.NY)
            .text("Lorem ipsum 2")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.ALL_EXCEPT_NY)
            .text("Lorem ipsum 2")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.ALL)
            .text("Lorem ipsum 2")
            .build()
    ));

    //when
    EmployerDisclaimers employerDisclaimers = disclaimers.listFor(employer);

    //then
    assertNotNull(employerDisclaimers);
    assertEquals(2, employerDisclaimers.size());
    assertEquals(State.NY, employerDisclaimers.get(0).getState());
    assertEquals(State.ALL, employerDisclaimers.get(1).getState());
  }


  @Test
  public void shouldReturnDisclaimersForAllStates() {
    //given
    Employer employer = Employer
        .builder()
        .state(State.CA)
        .fundingMethod(FundingMethod.ASO)
        .build();
    Disclaimers disclaimers = Disclaimers.of(of(
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.CA)
            .text("Lorem ipsum")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.ALL)
            .text("Lorem ipsum 2")
            .build()
    ));

    //when
    EmployerDisclaimers employerDisclaimers = disclaimers.listFor(employer);

    //then
    assertNotNull(employerDisclaimers);
    assertEquals(2, employerDisclaimers.size());
    assertEquals("Lorem ipsum", employerDisclaimers.get(0).getText());
    assertEquals("Lorem ipsum 2", employerDisclaimers.get(1).getText());
  }

  @Test
  public void shouldReturnDisclaimersInValidOrder() {
    //given
    Employer employer = Employer
        .builder()
        .state(State.CA)
        .fundingMethod(FundingMethod.ASO)
        .build();
    Disclaimers disclaimers = Disclaimers.of(of(
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.CA)
            .type(Type.LEGAL_ENTITY)
            .text("Legal entity")
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.CA)
            .text("Legal")
            .type(Type.LEGAL)
            .build(),
        EmployerDisclaimer.builder()
            .fundingMethod(FundingMethod.ASO)
            .state(State.CA)
            .text("State specific")
            .type(Type.STATE_SPECIFIC)
            .build()
    ));

    //when
    EmployerDisclaimers employerDisclaimers = disclaimers.listFor(employer);

    //then
    assertNotNull(employerDisclaimers);
    assertEquals(3, employerDisclaimers.size());
    assertEquals("Legal", employerDisclaimers.get(0).getText());
    assertEquals("State specific", employerDisclaimers.get(1).getText());
    assertEquals("Legal entity", employerDisclaimers.get(2).getText());
  }

  @Test
  public void shouldReturnDisclaimersForNonPlanPageType() {
    //given
    EmployerDisclaimers disclaimers = EmployerDisclaimers.of(of(
        EmployerDisclaimer.builder()
            .pagesConcerned(PagesConcerned.ALL)
            .text("A")
            .build(),
        EmployerDisclaimer.builder()
            .pagesConcerned(PagesConcerned.DENTAL)
            .text("B")
            .build(),
        EmployerDisclaimer.builder()
            .pagesConcerned(PagesConcerned.NOT_SELECTED)
            .text("C")
            .build()
    ));

    //when
    List<Disclaimer> employerDisclaimers = disclaimers.forNonPlanPage();

    //then
    assertNotNull(employerDisclaimers);
    assertEquals(1, employerDisclaimers.size());
    assertEquals("A", employerDisclaimers.get(0).getText());
  }

  @Test
  public void shouldReturnDisclaimersForPlanPageType() {
    //given
    EmployerDisclaimers disclaimers = EmployerDisclaimers.of(of(
        EmployerDisclaimer.builder()
            .pagesConcerned(PagesConcerned.ALL)
            .text("A")
            .build(),
        EmployerDisclaimer.builder()
            .pagesConcerned(PagesConcerned.VISION)
            .text("B")
            .build(),
        EmployerDisclaimer.builder()
            .pagesConcerned(PagesConcerned.HEALTH)
            .text("C")
            .build(),
        EmployerDisclaimer.builder()
            .pagesConcerned(PagesConcerned.NOT_SELECTED)
            .text("D")
            .build()
    ));

    //when
    List<Disclaimer> employerDisclaimers = disclaimers.forPlanPage(PlanType.VISION);

    //then
    assertNotNull(employerDisclaimers);
    assertEquals(2, employerDisclaimers.size());
    assertEquals("A", employerDisclaimers.get(0).getText());
    assertEquals("B", employerDisclaimers.get(1).getText());
  }
}