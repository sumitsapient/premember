package com.mirumagency.uhc.premember.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = AccordionGroup.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AccordionGroup extends WithVisibilityOptions {

  static final String RESOURCE_TYPE = "premember/components/content/accordionGroup";
  static final String TITLE_TYPE = "titleType";

  @ValueMapValue
  private String titleType;

  @ValueMapValue
  private String stylingVariant;

  @ValueMapValue
  private Boolean mobileOnly = false;

  @ValueMapValue
  private Boolean expandAsDefault = false;

  @ValueMapValue
  private String leftBorder = "none";

  public String getTitleType() {
    return titleType;
  }

  public String getStylingVariant() {
    return stylingVariant;
  }

  public Boolean isMobileOnly() {
    return mobileOnly;
  }

  public Boolean isExpandedAsDefault() {
    return expandAsDefault;
  }

  public Boolean isLeftBorderTealBlue() {
    return "teal-blue".equals(leftBorder);
  }

  public Boolean isLeftBorderTealBlueTextOnly() {
    return "teal-blue-text-only".equals(leftBorder);
  }
}