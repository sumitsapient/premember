package com.mirumagency.uhc.premember.core.domain.employer;

import static org.apache.http.util.Asserts.notNull;

import com.google.common.base.Strings;
import com.mirumagency.uhc.premember.core.domain.Link;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PromotionalSpace1 {

  private static final String PROMOTIONAL_SPACE_1_LINK_COPY = "promotionalSpace1LinkCopy";
  private static final String PROMOTIONAL_SPACE_1_LINK = "promotionalSpace1Link";
  private static final String PROMOTIONAL_SPACE_1_LINK_NEW_TAB = "promotionalSpace1LinkNewTab";

  public static final String DEFAULT_OPTION_1 = "Option1";

  public static PromotionalSpace1 of(EmployerCopy copy, NiceResource employerData) {
    notNull(copy, "copy");
    notNull(employerData, "employerData");

    String optionName =
        "promotionalSpace1" + employerData.getStringOrElse("promotionalSpace1Copy",
            DEFAULT_OPTION_1);

    String link = employerData.hasValue(PROMOTIONAL_SPACE_1_LINK)
        ? employerData.getString(PROMOTIONAL_SPACE_1_LINK)
        : copy.getOrEmpty(optionName + "Link");
    String linkText = employerData.hasValue(PROMOTIONAL_SPACE_1_LINK_COPY)
        ? employerData.getString(PROMOTIONAL_SPACE_1_LINK_COPY)
        : copy.getOrEmpty(optionName + "LinkCopy");

    boolean openInNewTab = employerData.hasValue(PROMOTIONAL_SPACE_1_LINK_NEW_TAB)
        ? employerData.getBoolean(PROMOTIONAL_SPACE_1_LINK_NEW_TAB)
        : false;

    return PromotionalSpace1.builder()
        .title(copy.getOrEmpty(optionName + "Title"))
        .description(copy.getOrEmpty(optionName + "Description"))
        .link(Link.builder()
            .link(link)
            .text(linkText)
            .target(openInNewTab ? "_blank" : "")
            .build())
        .build();
  }

  private final String title;
  private final String description;
  private final Link link;

}
