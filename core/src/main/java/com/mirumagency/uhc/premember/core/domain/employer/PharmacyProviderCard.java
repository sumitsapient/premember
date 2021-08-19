package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Getter;

@Getter
public class PharmacyProviderCard {

  private static final String DISPLAY_TOGGLE_PROP = "showPharmacyProviderInHealthPlanDetails";
  private static final String CUSTOM_LINK_PROP = "customPharmacyProviderLink";
  private static final String CUSTOM_LINK_TEXT_PROP = "customPharmacyProviderLinkText";
  private static final String DISPLAY_TEXT  = "pharmacyProviderDisplayText";

  private final boolean showPharmacyProviderInHealthPlanDetails;

  private final String customPharmacyProviderLink;

  private final String customPharmacyProviderLinkText;

  private final String pharmacyProviderDisplayText;

  public PharmacyProviderCard(boolean showPharmacyProviderInHealthPlanDetails, String customPharmacyProviderLink, String customPharmacyProviderLinkText, String pharmacyProviderDisplayText) {
    this.showPharmacyProviderInHealthPlanDetails = showPharmacyProviderInHealthPlanDetails;
    this.customPharmacyProviderLink = customPharmacyProviderLink;
    this.customPharmacyProviderLinkText = customPharmacyProviderLinkText;
    this.pharmacyProviderDisplayText = pharmacyProviderDisplayText;
  }

  public static PharmacyProviderCard of(NiceResource employerData) {
    return new PharmacyProviderCard(employerData.getBoolean(DISPLAY_TOGGLE_PROP), employerData.getString(CUSTOM_LINK_PROP),
        employerData.getString(CUSTOM_LINK_TEXT_PROP), PharmacyProviderDisplayText.of(employerData.getString(DISPLAY_TEXT)).getName());
  }
}
