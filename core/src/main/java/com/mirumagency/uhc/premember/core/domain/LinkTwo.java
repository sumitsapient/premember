package com.mirumagency.uhc.premember.core.domain;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Builder
@Getter
public class LinkTwo {

  final String link;
  final String text;
  @Default()
  final String target = "";

  public static LinkTwo empty() {
    return LinkTwo.builder().link("").text("").target("").build();
  }
}
