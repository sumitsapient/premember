package com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths;

import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.Path;

import static org.apache.http.util.Asserts.notNull;

public class ConfigurationPath extends Path {

    private static final String CONFIGURATION_PATH_TEMPLATE = "%s/en/configuration/jcr:content/root/responsivegrid";

    private final String root;

    public ConfigurationPath(EmployerPath employerPath) {
        notNull(employerPath, "employerPath");
        this.root = String.format(CONFIGURATION_PATH_TEMPLATE, employerPath.path());
    }

    public PlansListPath plansListPath(PlanType planType) {
        return new PlansListPath(this, planType);
    }

    @Override
    public String path() {
        return root;
    }
}
