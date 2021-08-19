package com.mirumagency.uhc.premember.core.domain.disclaimers;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;

@Getter
@Builder
public class LegalDisclaimersMappings {

  @Builder.Default
  private final List<LegalDisclaimerMapping> mappings = ImmutableList.of();

  public static LegalDisclaimersMappings of(List<LegalDisclaimerMapping> mappings) {
    return LegalDisclaimersMappings.builder().mappings(mappings).build();
  }

  public List<LegalDisclaimer> getDisclaimersFor(PlanOption option) {
    return mappings.stream()
        .filter(mapping -> mapping.isValidFor(option))
        .map(mapping -> new ImmutablePair<>(
            mapping,
            option.getDetails().getCopy().getOrEmpty(mapping.getDisclaimerTextCopyPropertyName())))
        .filter(pair -> isNotEmpty(pair.getRight()))
        .map(pair -> LegalDisclaimer.of(pair.getLeft().getVisibleOnPages(), pair.getRight()))
        .collect(toList());
  }

}
