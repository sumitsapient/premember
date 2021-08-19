package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;

import static org.apache.http.util.Asserts.notNull;

public class PlansListPath extends Path {

    private static final String EMPLOYER_PLANS_LIST_RESOURCE_SUFFIX = "Plans";
    private final String path;

    PlansListPath(ConfigurationPath configurationPath, PlanType planType) {
        notNull(configurationPath, "configurationPath");
        notNull(planType, "planType");
        path = configurationPath.path() + "/" + planType.getTypeName() + EMPLOYER_PLANS_LIST_RESOURCE_SUFFIX;
    }

    @Override
    public String path() {
        return path;
    }
}
