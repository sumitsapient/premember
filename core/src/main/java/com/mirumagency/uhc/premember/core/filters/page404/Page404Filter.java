package com.mirumagency.uhc.premember.core.filters.page404;

import static com.day.cq.wcm.api.WCMMode.DISABLED;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanDetailsPagePath.isPlanDetailsPagePath;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPagePath.getPlanPage;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPagePath.isPlanComparisonPath;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPagePath.isPlanSummaryPagePath;
import static java.lang.String.format;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import com.day.cq.wcm.api.WCMMode;
import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.exceptions.RequiredResourceNotFoundException;
import com.mirumagency.uhc.premember.core.services.EmployerService;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import com.mirumagency.uhc.premember.core.util.RequestCache;

public abstract class Page404Filter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    //not needed
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (isWcmDisabled(request) && isASlingRequest(request)) {
      SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
      SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) response;

      if (!doFilter(slingRequest, slingResponse)) {
        return;
      }
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    //not needed
  }

  private boolean doFilter(SlingHttpServletRequest slingRequest,
      SlingHttpServletResponse slingResponse) throws IOException, ServletException {
    return true;
  }

  public abstract EmployerService employerService();

  public abstract PlansAvailability createPlansAvailability(SlingHttpServletRequest slingRequest);

  protected PlansAvailability plansAvailability(SlingHttpServletRequest request) {
    return plansAvailability(request.getPathInfo(), getEmployer(request));
  }

  protected PlansAvailability plansAvailability(PlanType planType,
      SlingHttpServletRequest request) {
    return plansAvailability(getPlanSummaryPath(planType, request), getEmployer(request));
  }

  private PlansAvailability plansAvailability(String path, Employer employer) {
    if (isPlanDetailsPagePath(path)) {
      return new PlansAvailability4PlanDetails(path, employer);
    }

    if (isPlanSummaryPagePath(path) || isPlanComparisonPath(path)) {
      return new PlansAvailability4PlanSummary(path, employer);
    }

    return new PlansAvailability() {
    };
  }

  private String getPlanSummaryPath(PlanType planType, SlingHttpServletRequest slingRequest) {
    return String
        .format("%s.html", getPlanPage(EmployerPath.of(slingRequest.getPathInfo()), planType));
  }

  private boolean isASlingRequest(ServletRequest request) {
    return request instanceof SlingHttpServletRequest;
  }

  private boolean isWcmDisabled(ServletRequest request) {
    return WCMMode.fromRequest(request) == DISABLED;
  }

  private Employer getEmployer(SlingHttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    boolean includePlans = !employerService().isFederalSite(pathInfo);
    return RequestCache.of(request)
        .getEmployer((() -> employerService().loadEmployer(request.getPathInfo(),includePlans)));
  }

  private void redirectTo(SlingHttpServletRequest request,
      SlingHttpServletResponse response, String location)
      throws IOException {
    response.sendRedirect(format("%s.%s", location, request.getRequestPathInfo().getExtension()));
  }
}
