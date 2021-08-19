package com.mirumagency.uhc.premember.core.domain.disclaimers;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum PagesConcerned {
  NOT_SELECTED("not_selected", "Not selected", PlanType.NOT_SELECTED),
  ALL("all", "All pages", PlanType.NOT_SELECTED),
  HEALTH("health", "Health Plans pages", PlanType.HEALTH),
  VISION("vision", "Vision Plans pages", PlanType.VISION),
  DENTAL("dental", "Dental Plans pages", PlanType.DENTAL),
  FSA("fsa", "FSA Plans pages", PlanType.FSA),
  FINANCIAL("financial", "Financial Plans pages", PlanType.FINANCIAL),
  PHARMACY("pharmacy", "Pharmacy Plans pages", PlanType.PHARMACY);

  private final String code;
  private final String name;
  private final PlanType type;

  PagesConcerned(String code, String name, PlanType type) {
    this.code = code;
    this.name = name;
    this.type = type;
  }

  public static PagesConcerned of(NiceResource niceResource) {
    final String stringValue = niceResource.getString("pagesConcerned");
    return Arrays.stream(PagesConcerned.values())
        .filter(it -> stringValue.equals(it.code)).findFirst()
        .orElse(NOT_SELECTED);
  }
}
