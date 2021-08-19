package com.mirumagency.uhc.premember.core.services;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.Plans;
import com.mirumagency.uhc.premember.core.services.repository.PlansRepo;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = PlansService.class)
public class PlansService {

  @Reference
  private PlansRepo plansRepo;

  public Plans addPlans(Employer employer) {
    Plans plans = plansRepo.loadAll(employer.getPath());
    employer.withPlans(plans);
    return plans;
  }
}
