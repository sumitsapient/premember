package com.mirumagency.uhc.premember.core.domain;

import java.util.Map;
import java.util.Optional;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;

public class Logo {

    private static final String PATH_PROP = "employerLogoPath";
    private static final String DESCRIPTION_PROP = "dc:description";

    private final String path;
    private final String description;

    private Logo(String path,String description) {
        this.path = path;
        this.description = description;
    }

    public static Logo createLogo(NiceResource employerData, Map<String, Object> logoMetadata) {
        return new Logo(employerData.getString(PATH_PROP), description(logoMetadata));
    }

    private static String description(Map<String, Object> logoMetadata){
        return Optional.ofNullable(logoMetadata.get(DESCRIPTION_PROP))
            .map(Object::toString)
            .orElse("Employer logo");
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }
}
