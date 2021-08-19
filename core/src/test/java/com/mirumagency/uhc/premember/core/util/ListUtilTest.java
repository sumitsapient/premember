package com.mirumagency.uhc.premember.core.util;

import static com.google.common.collect.ImmutableList.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;

class ListUtilTest {

  @Test
  @SuppressWarnings("unchecked")
  void mergeAndRemoveDuplicates() {
    //when
    List<Object> mergedList = ListUtil
        .mergeAndRemoveDuplicates(of("123", 2, 33, 33), of(123, 33, 2));

    // then
    assertNotNull(mergedList);
    assertEquals(4, mergedList.size());
    assertEquals(mergedList.get(0), "123");
    assertEquals(mergedList.get(1), 2);
    assertEquals(mergedList.get(2), 33);
    assertEquals(mergedList.get(3), 123);
  }
}