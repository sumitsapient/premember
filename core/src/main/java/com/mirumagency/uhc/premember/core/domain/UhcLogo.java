package com.mirumagency.uhc.premember.core.domain;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;

public class UhcLogo {

    private static final String PATH_PROP = "uhcLogoPath";
    public static final String DEFAULT = "/content/dam/premember/logos/uhc/uhc_default_logo.png";

    private final String path;

    private UhcLogo(String path) {
        this.path = path;
    }

    public static UhcLogo of(NiceResource employerData) {
        return new UhcLogo(employerData.getStringOrElse(PATH_PROP, DEFAULT));
    }

    public String getPath() {
        return path;
    }
}
