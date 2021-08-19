package com.mirumagency.uhc.premember.core.services;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.disclaimers.Disclaimers;
import com.mirumagency.uhc.premember.core.domain.disclaimers.LegalDisclaimersMappings;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.services.repository.DisclaimersRepo;
import java.util.List;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = DisclaimersService.class)
public class DisclaimersService {

  @Reference
  private DisclaimersRepo disclaimersRepo;

  public void addDisclaimers(Employer employer) {
    addEmployerDisclaimers(employer);
    addLegalPlanOptionDisclaimers(employer);
  }

  private void addLegalPlanOptionDisclaimers(Employer employer) {
    LegalDisclaimersMappings mappings = disclaimersRepo.loadLegalDisclaimerMappings();
    employer.getPlans().getList().stream()
        .map(Plan::getOptions)
        .flatMap(List::stream)
        .forEach(option -> option.withLegalDisclaimers(mappings.getDisclaimersFor(option)));
  }

  private void addEmployerDisclaimers(Employer employer) {
    Disclaimers disclaimers = disclaimersRepo.loadDisclaimers();
    employer.withDisclaimers(disclaimers);
  }
}
