package com.mirumagency.uhc.premember.core.filters.page404;

import static com.mirumagency.uhc.premember.core.filters.page404.RedirectResult.noRedirect;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanDetailsPagePath;

class PlansAvailability4PlanDetails implements PlansAvailability {

  private final Employer employer;
  private final PlanDetailsPagePath planDetailsPagePath;

  public PlansAvailability4PlanDetails(String path, Employer employer) {
    this.employer = employer;
    this.planDetailsPagePath = new PlanDetailsPagePath(path);
  }

  @Override
  public long numberPlansAvailable() {
    return employer.getPlans()
        .getPlanOptionsByType(planDetailsPagePath.getPlanType())
        .stream()
        .filter(planDetailsPagePath::isForPlanOption)
        .count();
  }

  @Override
  public RedirectResult redirect() {
    return noRedirect();
  }
}
