package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;

public class OpenEnrollmentImage {

	private static final String PATH_PROP = "enrollmentImagePath";
	public static final String EMPTY = "";

	private final String path;

	private OpenEnrollmentImage(String path) {
		this.path = path;
	}

	public static OpenEnrollmentImage of(NiceResource employerData) {
		return new OpenEnrollmentImage(employerData.getStringOrElse(PATH_PROP, EMPTY));
	}

	public String getPath() {
		return path;
	}
}
