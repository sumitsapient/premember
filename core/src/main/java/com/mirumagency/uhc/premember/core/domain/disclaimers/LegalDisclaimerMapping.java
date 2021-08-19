package com.mirumagency.uhc.premember.core.domain.disclaimers;

import static com.mirumagency.uhc.premember.core.util.BooleanUtil.isTrue;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.http.util.Asserts.notNull;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import com.mirumagency.uhc.premember.core.domain.plans.PlanPage;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LegalDisclaimerMapping {

  private final PlanType relatedType;
  private final String planOptionPropertyName;
  private final String disclaimerTextCopyPropertyName;
  @Builder.Default
  private final List<PlanPage> visibleOnPages = ImmutableList.of();

  public static LegalDisclaimerMapping of(NiceResource resource) {
    notNull(resource, "resource");
    List<String> pages = resource.getList("visibleOnPages");
    return LegalDisclaimerMapping.builder()
        .relatedType(PlanType.from(resource.getString("relatedType")).orElse(PlanType.NOT_SELECTED))
        .visibleOnPages(pages.stream().map(PlanPage::from).collect(toList()))
        .planOptionPropertyName(resource.getString("planOptionPropertyName"))
        .disclaimerTextCopyPropertyName(resource.getString("disclaimerTextCopyPropertyName"))
        .build();
  }

  public boolean isValidFor(PlanOption option) {
    if (!relatedType.equals(option.getType())) {
      return false;
    }
    if (!option.contains(planOptionPropertyName)) {
      return false;
    }
    return isTrue(option.get(planOptionPropertyName));
  }

  public String getPages() {
    return visibleOnPages.stream()
        .map(PlanPage::toString)
        .collect(joining(", "));
  }
}
