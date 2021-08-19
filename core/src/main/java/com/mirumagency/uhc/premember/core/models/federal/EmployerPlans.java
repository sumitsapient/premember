package com.mirumagency.uhc.premember.core.models.federal;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = EmployerPlans.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmployerPlans {

    @ValueMapValue
    private String planType;

    public String getPlanType() {
        return StringUtils.capitalize(planType);
    }
}
