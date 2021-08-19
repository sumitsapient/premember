package com.mirumagency.uhc.premember.core.filters.page404;

import static com.mirumagency.uhc.premember.core.filters.page404.RedirectResult.noRedirect;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanDetailsPagePath.getPlanDetailsPage;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanDetailsPagePath.hasPlanDetailsPage;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPagePath;

class PlansAvailability4PlanSummary implements PlansAvailability {

  private final PlanPagePath planPagePath;
  private final Employer employer;

  PlansAvailability4PlanSummary(String path, Employer employer) {
    this.planPagePath = PlanPagePath.of(path);
    this.employer = employer;
  }

  @Override
  public long numberPlansAvailable() {
    return employer.getPlans().countByType(planPagePath.getPlanType());
  }

  @Override
  public RedirectResult redirect() {
    if (shouldRedirect()) {
      return RedirectResult.redirect(getPlanDetailsPageUrl());
    }
    return noRedirect();
  }

  private String getPlanDetailsPageUrl() {
    return getPlanDetailsPage(employer.getPath(),
        employer.getPlans().getFirstOptionByType(planPagePath.getPlanType()));
  }

  private boolean shouldRedirect() {
    return hasPlanDetailsPage(planPagePath.getPlanType()) && numberPlansAvailable() == 1;
  }
}
