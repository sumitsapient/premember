package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.apache.http.util.Asserts.notNull;

import com.mirumagency.uhc.premember.core.domain.plans.PlanType;

public class ConfigurationPath extends Path {

    private static final String CONFIGURATION_PATH_TEMPLATE = "%s/en/configuration/jcr:content/root/responsivegrid";

    private final String root;

    public ConfigurationPath(EmployerPath employerPath) {
        notNull(employerPath, "employerPath");
        this.root = String.format(CONFIGURATION_PATH_TEMPLATE, employerPath.path());
    }

    public EmployerDataConfigPath employerDataConfigPath() {
        return new EmployerDataConfigPath(this);
    }

    public PlansListPath plansListPath(PlanType planType) {
        return new PlansListPath(this, planType);
    }

    public FooterConfigPath footerConfigPath() {
        return new FooterConfigPath(this);
    }

    @Override
    public String path() {
        return root;
    }
}
