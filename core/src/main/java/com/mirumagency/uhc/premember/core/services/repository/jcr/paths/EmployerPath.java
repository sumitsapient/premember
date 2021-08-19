package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static com.mirumagency.uhc.premember.core.util.MapUtil.mapValue;
import static com.mirumagency.uhc.premember.core.util.MapUtil.toMap;
import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployerPath extends Path {

  private static final String BLUEPRINT_PATH = "/content/premember/employer-blueprint";
  private static final String EMPLOYER_PATH_REGEXP = "(/content/premember/.+)/en/.*";
  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final String path;

  private EmployerPath(String resourcePath) {
    this.path = calculateRoot(resourcePath);
  }

  public static EmployerPath of(String resourcePath) {
    notNull(resourcePath, "resourcePath");
    return new EmployerPath(resourcePath);
  }

  private String calculateRoot(String resourcePath) {
    if (resourcePath.startsWith(BLUEPRINT_PATH)) {
      return BLUEPRINT_PATH;
    }
    Pattern pattern = Pattern.compile(EMPLOYER_PATH_REGEXP);
    Matcher matcher = pattern.matcher(resourcePath);
    if (matcher.find()) {
      return matcher.group(1);
    }
    logger.warn(
        "Resource path is not under valid employer root: {}. Falling back to employer-blueprint: {}.",
        resourcePath, BLUEPRINT_PATH);
    return BLUEPRINT_PATH;
  }

  public ConfigurationPath configurationPath() {
    return new ConfigurationPath(this);
  }

  @Override
  public String path() {
    return path;
  }

  public EmployerDataConfigPath dataConfigPath() {
    return configurationPath().employerDataConfigPath();
  }

  public PlansListPath plansListPath(PlanType type) {
    return configurationPath().plansListPath(type);
  }

  public FooterConfigPath footerConfigPath() {
    return configurationPath().footerConfigPath();
  }

  public boolean isBlueprint() {
    return this.path.startsWith(BLUEPRINT_PATH);
  }

  public Map<String, Object> mapBlueprintUrls(Map<String, Object> props) {
    return props.entrySet().stream()
        .map(e -> mapValue(e, this::mapEmployerUrl))
        .collect(toMap());
  }

  private Object mapEmployerUrl(Object value) {
    if (!(value instanceof String)) {
      return value;
    }
    String string = (String) value;
    if (string.startsWith(BLUEPRINT_PATH)) {
      return string.replaceFirst(BLUEPRINT_PATH, path);
    }
    return string;
  }
}
