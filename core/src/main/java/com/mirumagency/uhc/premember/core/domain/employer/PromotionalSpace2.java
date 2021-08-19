package com.mirumagency.uhc.premember.core.domain.employer;

import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.Link;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PromotionalSpace2 {
  public static PromotionalSpace2 of(EmployerCopy copy, NiceResource employerData) {
    notNull(copy, "copy");
    notNull(employerData, "employerData");
    String optionName =
        "promotionalSpace2" + employerData.getStringOrElse("promotionalSpace2Copy", "Option1");


    return PromotionalSpace2.builder()
        .title(copy.getOrEmpty(optionName + "Title"))
        .description(copy.getOrEmpty(optionName + "Description"))
        .link(Link.builder().text(copy.getOrEmpty(optionName + "LinkCopy"))
            .link(copy.getOrEmpty(optionName + "Link")).build())
        .build();
  }

  private final String title;
  private final String description;
  private final Link link;

}
