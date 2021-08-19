package com.mirumagency.uhc.premember.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HashUtil {

  public static String hash(String value){
    int hash = 7;
    for (int i = 0; i < value.length(); i++) {
      hash = hash*31 + value.charAt(i);
    }
    return String.valueOf(Math.abs(hash));
  }

}
