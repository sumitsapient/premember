package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = PlanModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PlanModel extends TokensForEmployer {

    private Plan plan;

    private String planAsJson;

    private PlanOption selectedOption;

    @PostConstruct
    protected void initModel() {
        plan = plan();
        planAsJson = mapToJson(plan);
        selectedOption = plan.getSelectedOption();
    }

    public Plan getPlan() {
        return plan();
    }

    public String getPlanAsJson() {
        return planAsJson;
    }

    public List<PlanOption> getOptions() {
        return plan.getOptions();
    }

    public boolean hasMultipleOptions() {
        return plan.getOptions().size() > 1;
    }

    public PlanOption getSelectedOption() {
        return selectedOption;
    }

    public String getSelectedId() {
        return selectedOption.getId();
    }
}