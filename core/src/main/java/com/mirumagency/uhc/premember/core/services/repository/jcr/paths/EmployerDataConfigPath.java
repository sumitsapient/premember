package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.apache.http.util.Asserts.notNull;

public class EmployerDataConfigPath extends Path {

    private static final String EMPLOYER_DATA_CONFIG_RESOURCE = "employerData";
    private final String path;

    EmployerDataConfigPath(ConfigurationPath configurationPath) {
        notNull(configurationPath, "configurationPath");
        path = configurationPath.path() + "/" + EMPLOYER_DATA_CONFIG_RESOURCE;
    }

    @Override
    public String path() {
        return path;
    }
}
