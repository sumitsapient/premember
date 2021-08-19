package com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths;


import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.Path;
import static org.apache.http.util.Asserts.notNull;

public class EmployerPath extends Path {

  private static final String FEDERAL_ROOT_PATH = "/content/premember/employers/federal";
  private static final String FEDERAL_TEMPLATE_START_WITH_PATH = "/conf/premember/settings/wcm/templates/federal-";
  private final String path;

  private EmployerPath(String resourcePath) {
    this.path = calculateRoot(resourcePath);
  }

  public static EmployerPath of(String resourcePath) {
    notNull(resourcePath, "resourcePath");
    return new EmployerPath(resourcePath);
  }

  private String calculateRoot(String resourcePath) {
    if (!isFederalSite(resourcePath)) {
      throw new IllegalArgumentException("Invalid page or template path for Federal.");
    }
    return FEDERAL_ROOT_PATH;
  }

  public static boolean isFederalSite(String resourcePath){
    return resourcePath.startsWith(FEDERAL_ROOT_PATH) || resourcePath.startsWith(FEDERAL_TEMPLATE_START_WITH_PATH);
  }

  public ConfigurationPath configurationPath() {
    return new ConfigurationPath(this);
  }

  @Override
  public String path() {
    return path;
  }

  public PlansListPath plansListPath(PlanType type) {
    return configurationPath().plansListPath(type);
  }
}
