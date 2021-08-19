package com.mirumagency.uhc.premember.core.domain.disclaimers;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.plans.PlanPage;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LegalDisclaimer extends Disclaimer {

  @Builder.Default
  private final Type type = Type.LEGAL;
  @Builder.Default
  private final List<PlanPage> visibleOnPages = ImmutableList.of();
  private final String text;

  public static LegalDisclaimer of(List<PlanPage> visibleOnPages, String text) {
    return LegalDisclaimer.builder()
        .text(text)
        .visibleOnPages(visibleOnPages)
        .build();
  }

  public boolean isVisibleOn(PlanPage page) {
    return visibleOnPages.contains(page);
  }
}
