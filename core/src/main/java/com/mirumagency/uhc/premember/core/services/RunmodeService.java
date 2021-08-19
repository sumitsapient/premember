package com.mirumagency.uhc.premember.core.services;

import org.apache.sling.settings.SlingSettingsService;
import org.osgi.annotation.versioning.ProviderType;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

@ProviderType
@Component(service = RunmodeService.class, immediate = true)
@ServiceDescription("Allow to check the runmode of the instance")
public class RunmodeService {

  @Reference
  private SlingSettingsService settings;

  public boolean isAuthor() {
    return containsRunMode(Type.AUTHOR);
  }

  public boolean isProduction() {
    return containsRunMode(Type.PRD);
  }

  private boolean containsRunMode(Type type){
    return settings.getRunModes().contains(type.toString());
  }

  enum Type {
    AUTHOR, PRD;

    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }
}
