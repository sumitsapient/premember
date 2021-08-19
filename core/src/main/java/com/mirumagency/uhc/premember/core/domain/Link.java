package com.mirumagency.uhc.premember.core.domain;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Builder
@Getter
public class Link {

  final String link;
  final String text;
  @Default()
  final String target = "";

  public static Link empty() {
    return Link.builder().link("").text("").target("").build();
  }
}
