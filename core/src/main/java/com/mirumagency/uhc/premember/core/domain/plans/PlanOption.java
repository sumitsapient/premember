package com.mirumagency.uhc.premember.core.domain.plans;

import static com.google.common.collect.ImmutableList.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.http.util.Asserts.notNull;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.WithData;
import com.mirumagency.uhc.premember.core.domain.disclaimers.Disclaimer;
import com.mirumagency.uhc.premember.core.domain.disclaimers.LegalDisclaimer;
import com.mirumagency.uhc.premember.core.util.HashUtil;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Builder
public class PlanOption implements WithData {

  public static final String DEFAULT_VARIATION_NAME = "master";
  public static final String DEFAULT_NAME = "demo-plan-option";
  public static final ImmutableList<PlanType> PLANS_WITH_ONLY_SUMMARY_PAGE = of(PlanType.PHARMACY,
      PlanType.FSA, PlanType.FINANCIAL);
  public static final String DISCLAIMERS_PROP = "disclaimers";

  @Getter
  private final PlanType type;
  @Getter
  private final String name;
  @Getter
  private final String variation;
  @Getter
  private final PlanOptionDetails details;
  @Builder.Default
  private List<LegalDisclaimer> legalDisclaimers = of();

  public static PlanOption empty(PlanType planType) {
    notNull(planType, "planType");
    return builder()
        .type(planType)
        .name(DEFAULT_NAME)
        .variation(DEFAULT_VARIATION_NAME)
        .details(PlanOptionDetails.demo(planType))
        .build();
  }

  public static String id(String name, String hashedVariation) {
    String[] identifiers = new String[]{name, hashedVariation};
    return join(identifiers, "_");
  }

  public String getId() {
    return id(name, hashVariation());
  }

  public Map<String, String> getTokens() {
    return details.getTokens();
  }

  @Override
  public Map<String, Object> getData() {
    return this.getDetails().getData();
  }

  public String getOrEmpty(String prop) {
    return this.getDetails().getOrDefault(prop, "");
  }

  private String hashVariation() {
    return HashUtil.hash(variation);
  }

  public List<LegalDisclaimer> getDisclaimers() {
    ImmutableList.Builder<LegalDisclaimer> result = ImmutableList.builder();
    if (hasOptionSpecificDisclaimer()) {
      result.add(createOptionSpecificDisclaimer());
    }
    return result.addAll(legalDisclaimers).build();
  }

  public PlanOption withLegalDisclaimers(List<LegalDisclaimer> legalDisclaimers) {
    this.legalDisclaimers = ImmutableList.copyOf(legalDisclaimers);
    return this;
  }

  public List<Disclaimer> getDetailsPageDisclaimers() {
    return getDisclaimers().stream()
        .filter(it -> it.isVisibleOn(PlanPage.DETAILS))
        .collect(toList());
  }

  private boolean thisPlanHasOnlyTheSummaryPage() {
    return PLANS_WITH_ONLY_SUMMARY_PAGE.contains(type);
  }

  private boolean hasOptionSpecificDisclaimer() {
    return isNotEmpty(getOptionDisclaimerText());
  }

  private String getOptionDisclaimerText() {
    return this.getOrEmpty(DISCLAIMERS_PROP);
  }

  private LegalDisclaimer createOptionSpecificDisclaimer() {
    if (thisPlanHasOnlyTheSummaryPage()) {
      return LegalDisclaimer.of(of(PlanPage.SUMMARY), getOptionDisclaimerText());
    }
    return LegalDisclaimer.of(of(PlanPage.DETAILS), getOptionDisclaimerText());
  }
}
