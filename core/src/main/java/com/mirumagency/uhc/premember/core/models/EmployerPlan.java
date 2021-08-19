package com.mirumagency.uhc.premember.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;

import java.util.Optional;
import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = EmployerPlan.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmployerPlan extends Component {

  @SlingObject
  private ResourceResolver resourceResolver;

  @ValueMapValue
  private String fragmentPath;

  @ValueMapValue
  private String variationName;

  private String fragmentTitle;

  @PostConstruct
  protected void initModel() {
    resolveFragmentTitle();
  }

  private void resolveFragmentTitle() {
    fragmentTitle = Optional.ofNullable(fragmentPath)
        .map(resourceResolver::getResource)
        .map(NiceResource::of)
        .map(NiceResource::getContentOrThrow)
        .map(content -> content.getString(JcrConstants.JCR_TITLE))
        .orElse(StringUtils.EMPTY);
  }

  public String getFragmentPath() {
    return fragmentPath;
  }

  public String getFragmentTitle() {
    return fragmentTitle;
  }

  public String getVariationName() {
    return variationName;
  }

  public Boolean isFragmentPathNotBlank() {
    return StringUtils.isNotBlank(fragmentPath);
  }

  public Boolean isVariationNameNotBlank() {
    return StringUtils.isNotBlank(variationName);
  }
}
