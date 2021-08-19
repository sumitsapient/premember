package com.mirumagency.uhc.premember.core.services.repository.jcr;

import static java.util.stream.Collectors.toList;

import com.mirumagency.uhc.premember.core.domain.disclaimers.Disclaimers;
import com.mirumagency.uhc.premember.core.domain.disclaimers.EmployerDisclaimer;
import com.mirumagency.uhc.premember.core.domain.disclaimers.LegalDisclaimerMapping;
import com.mirumagency.uhc.premember.core.domain.disclaimers.LegalDisclaimersMappings;
import com.mirumagency.uhc.premember.core.services.repository.DisclaimersRepo;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import java.util.List;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = DisclaimersRepo.class)
public class DisclaimersRepoJcr implements DisclaimersRepo {

  private static final String DISCLAIMERS_CONFIGURATION_PATH =
      "/content/premember/disclaimer-configuration/jcr:content/root/responsivegrid/disclaimerList";
  private static final String DISCLAIMER_COMPONENT_RESOURCE_TYPE =
      "premember/components/configuration/disclaimers/disclaimer";
  private static final String LEGAL_DISCLAIMERS_CONFIGURATION_PATH =
      "/content/premember/legal-disclaimer-configuration/jcr:content/root/responsivegrid/disclaimerList";
  private static final String LEGAL_DISCLAIMER_COMPONENT_RESOURCE_TYPE =
      "premember/components/configuration/disclaimers/legalDisclaimer";

  @Reference
  private ResourceGentleman resourceGentleman;

  @Override
  public Disclaimers loadDisclaimers() {
    List<EmployerDisclaimer> list = resourceGentleman.withResolver(resolver ->
        resolver.getResourceOrThrow(DISCLAIMERS_CONFIGURATION_PATH)
            .getChildren()
            .filter(child -> child.isResourceType(DISCLAIMER_COMPONENT_RESOURCE_TYPE))
            .map(EmployerDisclaimer::of)
            .collect(toList()));
    return Disclaimers.of(list);
  }

  @Override
  public LegalDisclaimersMappings loadLegalDisclaimerMappings() {
    List<LegalDisclaimerMapping> mappings = resourceGentleman.withResolver(resolver ->
        resolver.getResourceOrThrow(LEGAL_DISCLAIMERS_CONFIGURATION_PATH)
            .getChildren()
            .filter(child -> child.isResourceType(LEGAL_DISCLAIMER_COMPONENT_RESOURCE_TYPE))
            .map(LegalDisclaimerMapping::of)
            .collect(toList()));
    return LegalDisclaimersMappings.of(mappings);
  }
}
