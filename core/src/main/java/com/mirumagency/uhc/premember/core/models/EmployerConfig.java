package com.mirumagency.uhc.premember.core.models;

import com.day.cq.wcm.api.Page;
import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.models.federal.FederalEmployerConfig;
import com.mirumagency.uhc.premember.core.models.plan.PlanWithUrls;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.mirumagency.uhc.premember.core.services.EmployerService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = EmployerConfig.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmployerConfig implements EmployerCommonConfig {
  @Inject
  private Page currentPage;

  @Inject
  private EmployerService federalEmployerService;

  @Self
  private PrememberEmployerConfig prememberEmployerConfig;

  @Self
  private FederalEmployerConfig federalEmployerConfig;

  private EmployerCommonConfig config;

  @PostConstruct
  protected void initModel() {
    boolean isFederalSite = federalEmployerService.isFederalSite(currentPage.getPath());
    if(!isFederalSite){
        config = prememberEmployerConfig;
    }else {
        config = federalEmployerConfig;
    }
  }

  public List<PlanWithUrls> getMainPlans() {
    return config.getMainPlans();
  }

  public String getUhcLogoPath() {
    return config.getUhcLogoPath();
  }

  public String homePagePath() {
    return config.homePagePath();
  }

  public boolean isEmployerLogoAdded() {
    return config.isEmployerLogoAdded();
  }

  public String getEmployerLogoPath() {
    return config.getEmployerLogoPath();
  }

  public String getEmployerLogoDescription(){
    return config.getEmployerLogoDescription();
  }

  public boolean hideSearchForPrescription() {
    return config.hideSearchForPrescription();
  }

  public Employer getEmployer() {
    return config.getEmployer();
  }

  public Map<String, String> getTokens() {
    return config.getTokens();
  }
}
