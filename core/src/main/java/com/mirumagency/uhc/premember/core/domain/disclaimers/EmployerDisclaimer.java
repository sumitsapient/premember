package com.mirumagency.uhc.premember.core.domain.disclaimers;

import static org.apache.http.util.Args.notNull;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmployerDisclaimer extends Disclaimer {

  public static EmployerDisclaimer of(NiceResource resource) {
    notNull(resource, "resource");
    return EmployerDisclaimer.builder()
        .type(Type.of(resource))
        .fundingMethod(FundingMethod.of(resource))
        .pagesConcerned(PagesConcerned.of(resource))
        .state(State.of(resource))
        .text(resource.getString("text"))
        .build();
  }

  @Builder.Default
  private final Type type = Type.NOT_SELECTED;
  private final FundingMethod fundingMethod;
  private final State state;
  private final PagesConcerned pagesConcerned;
  private final String text;

  public boolean concernsAllPages() {
    return pagesConcerned == PagesConcerned.ALL;
  }

  public boolean concernsPlanPage(PlanType type) {
    return concernsAllPages() || dedicatedFor(type);
  }

  private boolean dedicatedFor(PlanType type) {
    return pagesConcerned.getType() == type;
  }
}
