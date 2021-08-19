package com.mirumagency.uhc.premember.core.filters;

import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPagePath.PLAN_PAGE_REGEXP;
import static org.apache.sling.engine.EngineConstants.FILTER_SCOPE_REQUEST;

import javax.servlet.Filter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.mirumagency.uhc.premember.core.filters.page404.Page404Filter;
import com.mirumagency.uhc.premember.core.filters.page404.PlansAvailability;
import com.mirumagency.uhc.premember.core.services.EmployerService;

@Component(service = Filter.class,
    property = {
        EngineConstants.SLING_FILTER_SCOPE + "=" + FILTER_SCOPE_REQUEST,
        EngineConstants.SLING_FILTER_PATTERN + "=" + PLAN_PAGE_REGEXP,
        EngineConstants.SLING_FILTER_EXTENSIONS + "=" + "html"
    })
public class PlanPageFilter extends Page404Filter {

  @Reference
  private EmployerService employerService;

  @Override
  public EmployerService employerService() {
    return employerService;
  }

  @Override
  public PlansAvailability createPlansAvailability(SlingHttpServletRequest slingRequest) {
    return plansAvailability(slingRequest);
  }
}
