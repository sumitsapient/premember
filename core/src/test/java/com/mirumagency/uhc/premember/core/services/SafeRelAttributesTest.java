package com.mirumagency.uhc.premember.core.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.Attributes;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SafeRelAttributesTest {

  @Mock
  private Attributes attributes;

  @Test
  public void shouldThrowException_whenNullAttributesProvided() {
    assertThrows(IllegalStateException.class, () -> new SafeRelAttributes(null));
  }

  @Test
  public void shouldAddNoopenerAttribute_whenNoRelAttributes() {
    when(attributes.getValue("rel")).thenReturn(null);
    SafeRelAttributes attrs = new SafeRelAttributes(attributes);
    Attributes tested = attrs.getSafeAttrs();
    assertEquals("noopener", tested.getValue("rel"));
  }

  @Test
  public void shouldAppendNoopener_whenOnlyNoreferSet() {
    when(attributes.getValue("rel")).thenReturn("noreferer");
    SafeRelAttributes attrs = new SafeRelAttributes(attributes);
    Attributes tested = attrs.getSafeAttrs();
    assertEquals("noreferer noopener", tested.getValue("rel"));
  }

  @Test
  public void shouldNotChangeRelAttributes_whenNoopenerAmongOthersSet() {
    when(attributes.getValue("rel")).thenReturn("author noopener license pingback");
    SafeRelAttributes attrs = new SafeRelAttributes(attributes);
    Attributes tested = attrs.getSafeAttrs();
    assertEquals("author noopener license pingback", tested.getValue("rel"));
  }

  @Test
  public void shouldNotChangeRelAttributes_whenOnlyNoopenerSet() {
    when(attributes.getValue("rel")).thenReturn("noopener");
    SafeRelAttributes attrs = new SafeRelAttributes(attributes);
    Attributes tested = attrs.getSafeAttrs();
    assertEquals("noopener", tested.getValue("rel"));
  }

  @Test
  public void shouldAppendNoopener_whenNorefererSet() {
    when(attributes.getValue("rel")).thenReturn("pingback noreferer preload");
    SafeRelAttributes attrs = new SafeRelAttributes(attributes);
    Attributes tested = attrs.getSafeAttrs();
    assertEquals("pingback noreferer preload noopener", tested.getValue("rel"));
  }

  @Test
  public void shouldAppendNoopener_whenRelAttributesAlreadyExists() {
    when(attributes.getValue("rel")).thenReturn("author license pingback");
    SafeRelAttributes attrs = new SafeRelAttributes(attributes);
    Attributes tested = attrs.getSafeAttrs();
    assertEquals("author license pingback noopener", tested.getValue("rel"));
  }
}
