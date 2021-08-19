package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static org.apache.http.util.Asserts.notNull;

public class FooterConfigPath extends Path {

    private static final String FOOTER_CONFIG_RESOURCE = "footerConfig";
    private final String path;

    FooterConfigPath(ConfigurationPath configurationPath) {
        notNull(configurationPath, "configurationPath");
        path = configurationPath.path() + "/" + FOOTER_CONFIG_RESOURCE;
    }

    @Override
    public String path() {
        return path;
    }
}
