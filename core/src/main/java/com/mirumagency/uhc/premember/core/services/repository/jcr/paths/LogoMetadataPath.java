package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;

public class LogoMetadataPath extends Path {

  private static String METADATA_TEMPATE_PATH = "%s/jcr:content/metadata";
  private static final String LOGO_PATH_PROP = "employerLogoPath";
  private String path;

  private LogoMetadataPath(String logoPath) {
    this.path = String.format(METADATA_TEMPATE_PATH, logoPath);
  }

  public static LogoMetadataPath of(NiceResource employerDataConfigResource) {
    return new LogoMetadataPath(employerDataConfigResource.getString(LOGO_PATH_PROP));
  }

  @Override
  public String path() {
    return path;
  }
}
