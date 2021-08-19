package com.mirumagency.uhc.premember.core.domain.employer;

import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PromotionalAreas {

  public static PromotionalAreas of(EmployerCopy copy, NiceResource employerData) {
    notNull(copy, "copy");
    notNull(employerData, "employerData");

    boolean showSpace1 = employerData.isTrue("showPromotionalSpace1");

    PromotionalSpace1 space1 = PromotionalSpace1.of(copy, employerData);
    PromotionalSpace2 space2 = PromotionalSpace2.of(copy, employerData);

    return PromotionalAreas.builder()
        .showSpace1(showSpace1)
        .space1(space1)
        .space2(space2)
        .build();
  }

  private final boolean showSpace1;
  private final PromotionalSpace1 space1;
  private final PromotionalSpace2 space2;

}
