package com.mirumagency.uhc.premember.core.models.disclaimers;

import com.mirumagency.uhc.premember.core.domain.disclaimers.EmployerDisclaimer;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

@Getter
@Model(adaptables = SlingHttpServletRequest.class,
    adapters = DisclaimerModel.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DisclaimerModel {

  @ScriptVariable
  private Resource resource;

  private EmployerDisclaimer disclaimer;

  @PostConstruct
  protected void initModel() {
    disclaimer = EmployerDisclaimer.of(NiceResource.of(resource));
  }
}
