package com.mirumagency.uhc.premember.core.domain.plans;

import static java.util.Arrays.stream;

public enum PlanPage {
  NOT_SELECTED, DETAILS, SUMMARY, COMPARISON_TABLE;

  public static PlanPage from(String name) {
    return stream(PlanPage.values())
        .filter(it -> it.name().equals(name))
        .findFirst()
        .orElse(NOT_SELECTED);
  }
}
