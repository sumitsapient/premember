package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanPdl;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = ProviderCardList.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProviderCardList extends TokensForEmployer {

  private Map<String, String> pharmacyPlanTokens;

  private List<PlanPdl> customPdls;

  @PostConstruct
  protected void initModel() {
    Plan plan = getEmployer().getPlans().getPlanByType(PlanType.PHARMACY);
    pharmacyPlanTokens = plan.getTokens();
    customPdls = plan.getCustomPdls();
  }

  @Override
  public Map<String, String> getTokens() {
    return pharmacyPlanTokens;
  }

  public List<PlanPdl> getCustomPdls() {
    return customPdls;
  }
}
