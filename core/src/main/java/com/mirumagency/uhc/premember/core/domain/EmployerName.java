package com.mirumagency.uhc.premember.core.domain;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class EmployerName {

  private final String name;

  static EmployerName of(NiceResource employerResource) {
    return new EmployerName(giveEmployerName(employerResource));
  }

  static String giveEmployerName(NiceResource employerResource) {
    return employerResource.getChildOrThrow("jcr:content").getString("jcr:title");
  }

  public static EmployerName empty() {
    return new EmployerName("");
  }

  public Map<String, Object> getTokens() {
    return ImmutableMap.of("employerName", name);
  }
}
