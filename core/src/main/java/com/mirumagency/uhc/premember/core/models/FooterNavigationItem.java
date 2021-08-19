package com.mirumagency.uhc.premember.core.models;


import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Getter
@Model(adaptables = Resource.class,
    adapters = FooterNavigationItem.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterNavigationItem {

  @Inject
  private String displayText;

  @Inject
  @Setter
  private String url;

  @Inject
  private Boolean openInNewTab;
}
