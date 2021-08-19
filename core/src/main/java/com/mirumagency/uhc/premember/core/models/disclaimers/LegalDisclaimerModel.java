package com.mirumagency.uhc.premember.core.models.disclaimers;

import com.mirumagency.uhc.premember.core.domain.disclaimers.LegalDisclaimerMapping;
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
    adapters = LegalDisclaimerModel.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class LegalDisclaimerModel {

  @ScriptVariable
  private Resource resource;

  private LegalDisclaimerMapping mapping;

  @PostConstruct
  protected void initModel() {
    mapping = LegalDisclaimerMapping.of(NiceResource.of(resource));
  }
}
