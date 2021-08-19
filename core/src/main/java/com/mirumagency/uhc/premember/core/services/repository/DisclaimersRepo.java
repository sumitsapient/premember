package com.mirumagency.uhc.premember.core.services.repository;

import com.mirumagency.uhc.premember.core.domain.disclaimers.Disclaimers;
import com.mirumagency.uhc.premember.core.domain.disclaimers.LegalDisclaimersMappings;

public interface DisclaimersRepo {

  Disclaimers loadDisclaimers();

  LegalDisclaimersMappings loadLegalDisclaimerMappings();
}
