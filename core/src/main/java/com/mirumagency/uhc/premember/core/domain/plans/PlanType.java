package com.mirumagency.uhc.premember.core.domain.plans;

import static java.util.Arrays.stream;

import java.util.Optional;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@Getter
public enum PlanType {
  HEALTH("health"),
  VISION("vision"),
  DENTAL("dental"),
  FSA("fsa"),
  FINANCIAL("financial"),
  PHARMACY("pharmacy"),
  BEHAVIORAL_HEALTH("behavioralhealth"),
  NOT_SELECTED("not_selected");

  private final String typeName;

  PlanType(String typeName) {
    this.typeName = typeName;
  }

  public static Optional<PlanType> from(String typeName) {
    return stream(values())
        .filter(type -> StringUtils.equalsIgnoreCase(type.typeName, typeName))
        .findFirst();
  }
}
