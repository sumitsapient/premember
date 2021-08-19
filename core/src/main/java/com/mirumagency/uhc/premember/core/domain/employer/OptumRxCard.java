package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Getter;

@Getter
public class OptumRxCard {

  private static final String DISPLAY_TOGGLE_PROP = "showOptumRxInHealthPlanDetails";
  private static final String CUSTOM_LINK_PROP = "customOptumRxLink";
  private static final String CUSTOM_LINK_TEXT_PROP = "customOptumRxLinkText";

  private final boolean showOptumRxInHealthPlanDetails;

  private final String customOptumRxLink;

  private final String customOptumRxLinkText;

  public OptumRxCard(boolean showOptumRxInHealthPlanDetails, String customOptumRxLink, String customOptumRxLinkText) {
    this.showOptumRxInHealthPlanDetails = showOptumRxInHealthPlanDetails;
    this.customOptumRxLink = customOptumRxLink;
    this.customOptumRxLinkText = customOptumRxLinkText;
  }

  public static OptumRxCard of(NiceResource employerData) {
    return new OptumRxCard(employerData.getBoolean(DISPLAY_TOGGLE_PROP), employerData.getString(CUSTOM_LINK_PROP),
        employerData.getString(CUSTOM_LINK_TEXT_PROP));
  }
}
