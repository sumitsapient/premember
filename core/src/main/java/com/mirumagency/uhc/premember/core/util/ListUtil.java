package com.mirumagency.uhc.premember.core.util;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ListUtil {

  @SuppressWarnings("unchecked")
  public static <T> List<T> mergeAndRemoveDuplicates(List<T>... lists) {
    Set<T> sortedSet = new LinkedHashSet<T>() {{
      Arrays.stream(lists).forEach(this::addAll);
    }};
    return ImmutableList.copyOf(sortedSet);
  }
}
