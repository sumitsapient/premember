package com.mirumagency.uhc.premember.core.domain.plans;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.Copy;
import com.mirumagency.uhc.premember.core.domain.disclaimers.LegalDisclaimer;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
public class PlanCopy extends Copy {

  private static final String DISCLAIMERS_COMPARISON_PROP = "comparisonDisclaimer";

  public PlanCopy(Map<String, Object> data) {
    super(data);
  }

  public static PlanCopy empty() {
    return new PlanCopy(ImmutableMap.of());
  }

  public static PlanCopy of(Map<String, Object> data) {
    return new PlanCopy(data);
  }

  public List<LegalDisclaimer> getDisclaimers() {
    if (contains(DISCLAIMERS_COMPARISON_PROP)) {
      return ImmutableList.of(
          LegalDisclaimer.of(
              ImmutableList.of(PlanPage.COMPARISON_TABLE),
              getOrEmpty(DISCLAIMERS_COMPARISON_PROP)
          ));
    }
    return ImmutableList.of();
  }
}
