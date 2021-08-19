package com.mirumagency.uhc.premember.core.services;

import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.EmployerPath;
import com.mirumagency.uhc.premember.core.services.repository.EmployerRepo;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = EmployerService.class)
public class EmployerService {

  @Reference
  private PlansService plansService;

  @Reference
  private DisclaimersService disclaimersService;

  @Reference
  private EmployerRepo employerRepo;

  public Employer loadEmployer(String resourcePath, boolean includePlans) {
    notNull(resourcePath, "resourcePath");
    Employer employer = employerRepo.load(resourcePath);
    if(includePlans) {
      plansService.addPlans(employer);
      disclaimersService.addDisclaimers(employer);
    }
    return employer;
  }

  public boolean isFederalSite(String resourcePath){
    return EmployerPath.isFederalSite(resourcePath);
  }
}