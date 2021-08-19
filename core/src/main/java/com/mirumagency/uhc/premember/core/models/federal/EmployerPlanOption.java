package com.mirumagency.uhc.premember.core.models.federal;

import com.mirumagency.uhc.premember.core.models.Component;
import com.mirumagency.uhc.premember.core.services.federal.PlansService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Model(adaptables = {Resource.class},
    adapters = EmployerPlanOption.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmployerPlanOption extends Component {

	@ValueMapValue @Default(values= EMPTY)
	private String id;

	@ValueMapValue  @Default(values= EMPTY)
	private String planType;

  private String planName;

	@Inject
	private PlansService plansService;

  @PostConstruct
  protected void initModel() {
  		planName = plansService.getPlanName(planType, id);
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return planName;
  }

  public String getPlanType() { return planType; }

}
