package com.mirumagency.uhc.premember.core.domain.disclaimers;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Delegate;

@Builder
@Getter
public class EmployerDisclaimers {

  @Delegate
  private final List<EmployerDisclaimer> list;

  public static EmployerDisclaimers empty() {
    return EmployerDisclaimers.of(ImmutableList.of());
  }

  public static EmployerDisclaimers of(List<EmployerDisclaimer> list) {
    return EmployerDisclaimers.builder().list(list).build();
  }

  public List<Disclaimer> forNonPlanPage() {
    return list.stream()
        .filter(EmployerDisclaimer::concernsAllPages)
        .collect(toList());
  }

  public List<Disclaimer> forPlanPage(PlanType type) {
    return list.stream()
        .filter(disclaimer -> disclaimer.concernsPlanPage(type))
        .collect(toList());
  }
}
