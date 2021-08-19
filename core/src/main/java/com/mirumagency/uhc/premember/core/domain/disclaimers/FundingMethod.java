package com.mirumagency.uhc.premember.core.domain.disclaimers;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum FundingMethod {
  NOT_SELECTED("Not selected"),
  ALL("All methods"),
  ASO("ASO"),
  FI("FI"),
  HMO("HMO"),
  OXFORD_FI("Oxford-FI"),
  OXFORD_ASO("Oxford-ASO");

  private final String name;

  FundingMethod(String name) {
    this.name = name;
  }

  public static FundingMethod of(NiceResource niceResource) {
    final String stringValue = niceResource.getString("fundingMethod");
    return Arrays.stream(FundingMethod.values())
        .filter(it -> stringValue.equals(it.name)).findFirst()
        .orElse(NOT_SELECTED);
  }
}
