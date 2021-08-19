package com.mirumagency.uhc.premember.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Util class for normalizing text for analytics requirements
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AnalyticsUtil {

  // all non-ascii characters
  private static final String REMOVE_CHARACTERS_REGEX = "[^\\x00-\\x7F]";

  public static String normalize(String text) {
    if (text == null) {
      return "";
    }
    return text.toLowerCase()
        .replace(' ', '_')
        .replaceAll(REMOVE_CHARACTERS_REGEX, "");
  }
}
