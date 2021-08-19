package com.mirumagency.uhc.premember.core.domain.employer;

import static org.apache.http.util.Asserts.notNull;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.Link;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Builder
@Getter
public class Healthcare101 {

  private static final String HEALTHCARE_101_LINK_COPY = "healthcare101LinkCopy";
  private static final String HEALTHCARE_101_LINK = "healthcare101Link";
  private static final String HEALTHCARE_101_LINK_NEW_TAB = "healthcare101LinkNewTab";

  public static final String DEFAULT_OPTION_1 = "Option1";

  public static Healthcare101 of(EmployerCopy copy, NiceResource employerData) {
    notNull(copy, "copy");
    notNull(employerData, "employerData");

    String optionName =
        "healthcare101" + employerData.getStringOrElse("healthcare101Copy", DEFAULT_OPTION_1);

    String link = employerData.hasValue(HEALTHCARE_101_LINK)
        ? employerData.getString(HEALTHCARE_101_LINK)
        : copy.getOrEmpty(optionName + "Link");
    String linkText = employerData.hasValue(HEALTHCARE_101_LINK_COPY)
        ? employerData.getString(HEALTHCARE_101_LINK_COPY)
        : copy.getOrEmpty(optionName + "LinkCopy");

    boolean openInNewTab = employerData.hasValue(HEALTHCARE_101_LINK_NEW_TAB)
        ? employerData.getBoolean(HEALTHCARE_101_LINK_NEW_TAB)
        : false;

    return Healthcare101.builder()
        .title(copy.getOrEmpty(optionName + "Title"))
        .description(copy.getOrEmpty(optionName + "Description"))
        .linkOpenInNewTab(openInNewTab)
        .link(Link.builder()
            .link(link)
            .text(linkText)
            .target(openInNewTab ? "_blank" : "")
            .build())
        .build();
  }

  public static Healthcare101 empty() {
    return Healthcare101.builder().build();
  }

  public Map<String, String> getTokens() {
    return ImmutableMap.<String, String>builder()
        .put("healthcare101Title", title)
        .put("healthcare101Description", description)
        .put("healthcare101LinkFinal", link.getLink())
        .put("healthcare101LinkCopyFinal", link.getText())
        .put("healthcare101OpenInNewTab", linkOpenInNewTab.toString())
        .build();
  }

  @Default
  private final String title = "";
  @Default
  private final String description = "";
  @Default
  private final Boolean linkOpenInNewTab = false;
  @Default
  private final Link link = Link.empty();
}
