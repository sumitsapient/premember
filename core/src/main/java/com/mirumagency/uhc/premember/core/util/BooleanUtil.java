package com.mirumagency.uhc.premember.core.util;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BooleanUtil {

  public static boolean isTrue(Object value) {
    if (value instanceof Boolean) {
      return TRUE.equals(value);
    }
    return TRUE.toString().equals(value);
  }

  public static boolean isFalse(Object value) {
    if (value instanceof Boolean) {
      return FALSE.equals(value);
    }
    return FALSE.toString().equals(value);
  }
}
