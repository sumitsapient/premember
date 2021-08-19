package com.mirumagency.uhc.premember.core.domain.plans;

import static com.mirumagency.uhc.premember.core.util.MapUtil.mapKey;
import static com.mirumagency.uhc.premember.core.util.MapUtil.toMap;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.disclaimers.Disclaimer;
import com.mirumagency.uhc.premember.core.domain.disclaimers.LegalDisclaimer;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.ToString;

@ToString
@Builder
public class Plan {

  @Default
  private final PlanType type = PlanType.NOT_SELECTED;
  @Default
  private final List<PlanOption> options = ImmutableList.of();
  private String selectedOptionId;
  @Default
  private PlanCopy copy = PlanCopy.empty();

  @Default
  private final List<PlanPdl> customPdls = ImmutableList.of();

  public static Plan empty(PlanType type) {
    return Plan.builder().type(type).build();
  }

  public PlanOption getSelectedOption() {
    if (getOptions().isEmpty()) {
      return PlanOption.empty(type);
    }
    if (selectedOptionId == null) {
      return getOptions().get(0);
    }
    return getOptions().stream()
        .filter(o -> o.getId().equals(selectedOptionId))
        .findFirst()
        .orElse(getOptions().get(0));
  }

  public PlanType getType() {
    return type;
  }

  public List<PlanOption> getOptions() {
    return Optional.ofNullable(options).orElse(ImmutableList.of());
  }

  public PlanOption getFirstOption() {
    return getOptions().stream().findFirst().orElse(PlanOption.empty(getType()));
  }

  public int optionsCount() {
    return getOptions().size();
  }

  public void setCopy(PlanCopy copy) {
    this.copy = copy;
  }

  public PlanCopy getCopy() {
    return Optional.ofNullable(copy).orElse(PlanCopy.empty());
  }

  public Map<String, String> getTokens() {
    return ImmutableMap
        .<String, String>builder()
        .putAll(copyTokens())
        .putAll(selectedOptionTokens())
        .build();
  }

  public Plan withSelectedOptionId(String selectedOptionId) {
    this.selectedOptionId = selectedOptionId;
    return this;
  }

  private Map<String, String> selectedOptionTokens() {
    return getSelectedOption().getTokens().entrySet().stream()
        .map(e -> mapKey(e, key -> "selectedOption." + key))
        .collect(toMap());
  }

  private Map<String, String> copyTokens() {
    return getCopy().tokens().entrySet().stream()
        .map(e -> mapKey(e, key -> "copy." + key))
        .collect(toMap());
  }

  public boolean notEmpty() {
    return !getOptions().isEmpty();
  }

  public List<LegalDisclaimer> getDisclaimers() {
    return ImmutableList.<LegalDisclaimer>builder()
        .addAll(optionDisclaimers())
        .addAll(copy.getDisclaimers())
        .build();
  }

  private List<LegalDisclaimer> optionDisclaimers() {
    return this.getOptions().stream()
        .map(PlanOption::getDisclaimers)
        .flatMap(List::stream)
        .collect(toList());
  }

  public List<Disclaimer> getSummaryPageDisclaimers() {
    return getDisclaimers().stream()
        .filter(it -> it.isVisibleOn(PlanPage.SUMMARY))
        .collect(toList());
  }

  public List<Disclaimer> getComparisonTablePageDisclaimers() {
    return getDisclaimers().stream()
        .filter(it -> it.isVisibleOn(PlanPage.COMPARISON_TABLE))
        .collect(toList());
  }

  public List<PlanPdl> getCustomPdls() {
    return Optional.ofNullable(customPdls).orElse(ImmutableList.of());
  }
}
