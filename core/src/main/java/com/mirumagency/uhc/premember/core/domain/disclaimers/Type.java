package com.mirumagency.uhc.premember.core.domain.disclaimers;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum Type {
  NOT_SELECTED("Not selected"),
  LEGAL("Legal"),
  STATE_SPECIFIC("State specific"),
  LEGAL_ENTITY("Legal Entity");

  public static final List<Type> SORTING_ORDER = ImmutableList
      .of(LEGAL, STATE_SPECIFIC, LEGAL_ENTITY);

  private final String name;

  Type(String name) {
    this.name = name;
  }

  public static Type of(NiceResource niceResource) {
    final String stringValue = niceResource.getString("type");
    return Arrays.stream(Type.values())
        .filter(it -> stringValue.equals(it.toString())).findFirst()
        .orElse(NOT_SELECTED);
  }

  public Integer getOrder() {
    return SORTING_ORDER.indexOf(this);
  }
}
