package com.mirumagency.uhc.premember.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsUtilTest {

  @Test
  void shouldRemoveNonAsciiCharacters() {
    String tested = "@©®™~!@#$%^&*()_+-={}[]:\";'<>,./?'";
    assertEquals("@~!@#$%^&*()_+-={}[]:\";'<>,./?'", AnalyticsUtil.normalize(tested));
  }

  @Test
  void shouldReplaceSpaces() {
    String tested = "  ";
    assertEquals("__", AnalyticsUtil.normalize(tested));
  }

  @Test
  void shouldChangeTextToLowercase() {
    String tested = "ENHANCEDPLAN";
    assertEquals("enhancedplan", AnalyticsUtil.normalize(tested));
  }
}