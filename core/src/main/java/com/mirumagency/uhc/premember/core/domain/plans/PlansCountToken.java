package com.mirumagency.uhc.premember.core.domain.plans;

public class PlansCountToken {

  private static final String PLANS_COUNT_TOKEN_SUFFIX = "PlansCount";

  private final String tokenName;
  private final Integer count;

  public PlansCountToken(PlanType planType, int count) {
    this.tokenName = resolveTokenName(planType);
    this.count = count;
  }

  public String getTokenName() {
    return tokenName;
  }

  public int getCount() {
    return count;
  }

  public String getOptionsTokenValue() {
    if (count == 1) {
      return "1 option";
    }
    return count + " options";
  }

  private String resolveTokenName(PlanType planType) {
    return planType.getTypeName() + PLANS_COUNT_TOKEN_SUFFIX;
  }
}
