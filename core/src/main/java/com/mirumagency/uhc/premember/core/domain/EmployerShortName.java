package com.mirumagency.uhc.premember.core.domain;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class EmployerShortName {

  private final String shortName;

  public static EmployerShortName of(NiceResource employerResource) {
    return new EmployerShortName(giveEmployerShortName(employerResource));
  }

	static String giveEmployerShortName(NiceResource employerResource) {
		return employerResource.getChildOrThrow("jcr:content").getParentOrThrow().getName();
	}

  public static EmployerShortName empty() {
    return new EmployerShortName("");
  }

  public Map<String, Object> getTokens() {
    return ImmutableMap.of("employerShortName", shortName);
  }
}