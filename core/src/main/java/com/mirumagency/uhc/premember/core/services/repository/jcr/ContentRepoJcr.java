package com.mirumagency.uhc.premember.core.services.repository.jcr;

import static com.mirumagency.uhc.premember.core.util.MapUtil.mapValue;
import static com.mirumagency.uhc.premember.core.util.MapUtil.toMap;

import com.day.cq.commons.jcr.JcrConstants;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mirumagency.uhc.premember.core.domain.employer.EmployerCopy;
import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanCopyPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResourceResolver;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = ContentRepoJcr.class)
public class ContentRepoJcr {

  private static final List<String> JCR_RELATED_PROPERTIES =
      Lists.newArrayList(JcrConstants.JCR_PRIMARYTYPE, JcrConstants.JCR_MIXINTYPES,
          JcrConstants.JCR_LASTMODIFIED, JcrConstants.JCR_LAST_MODIFIED_BY);

  private static final String EMPLOYERS_COPY_PATH = "/content/dam/premember/employers/employers-copy/jcr:content/data/master";

  @Reference
  private ResourceGentleman resourceGentleman;

  public EmployerCopy loadEmployerCopy(EmployerPath path) {
    return EmployerCopy.of(loadData(EMPLOYERS_COPY_PATH, path));
  }

  public PlanCopy loadPlanCopy(PlanType type, EmployerPath path) {
    return PlanCopy.of(loadData(resolvePlanCopyPath(type), path));
  }

  public Map<String, Object> loadData(String resourcePath, EmployerPath path) {
    return resourceGentleman.withResolver(resolver -> resolver
        .getResource(resourcePath)
        .map(NiceResource::getValueMap)
        .map(this::filterProperties)
        .map(path::mapBlueprintUrls)
        .map(prop -> shortenEmployerUrls(prop, path, resolver))
        .orElseGet(ImmutableMap::of));
  }

  private Map<String, Object> shortenEmployerUrls(Map<String, Object> props, EmployerPath path,
      NiceResourceResolver resolver) {
    return props.entrySet().stream()
        .map(e -> mapValue(e, v -> this.shortenEmployerUrl(v, path, resolver)))
        .collect(toMap());
  }

  private Object shortenEmployerUrl(Object value, EmployerPath path,
      NiceResourceResolver resolver) {
    if (!(value instanceof String)) {
      return value;
    }
    String string = (String) value;
    if (string.startsWith(path.path())) {
      return Optional.ofNullable(resolver.map(string)).orElse(string);
    }
    return string;
  }

  private Map<String, Object> filterProperties(Map<String, Object> properties) {
    return properties.entrySet().stream()
        .filter(entry -> !entry.getKey().contains("@"))
        .filter(entry -> !JCR_RELATED_PROPERTIES.contains(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private String resolvePlanCopyPath(PlanType type) {
    PlanCopyPath copyPath = PlanCopyPath.of(type);
    return copyPath.path();
  }
}
