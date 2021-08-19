package com.mirumagency.uhc.premember.core.services;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import static org.apache.http.util.Asserts.notNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

final class SafeRelAttributes {

  private static final String NOOPENER_VALUE = "noopener";

  private static final String NOREFERER_VALUE = "noreferer";

  private static final String REL_ATTR = "rel";

  private final Attributes attributes;

  SafeRelAttributes(Attributes attributes) {
    notNull(attributes, "attributes");
    this.attributes = attributes;
  }

  Attributes getSafeAttrs() {
    Attributes result = attributes;
    if (!hasRelNoopener()) {
      result = getSafeRelAttrs();
    }
    return result;
  }

  private AttributesImpl getSafeRelAttrs() {
    AttributesImpl result = new AttributesImpl(attributes);
    result.addAttribute("", REL_ATTR, REL_ATTR, "CDATA", getRelAttrsWithNoopener());
    return result;
  }

  private String getRelAttrsWithNoopener() {
    String relAttrs = attributes.getValue(REL_ATTR);
    if (relAttrs == null) {
      relAttrs = "noopener";
    } else if (!hasRelNoopener()) {
      relAttrs += " noopener";
    }
    return relAttrs;
  }

  private boolean hasRelNoopener() {
    return StringUtils.contains(attributes.getValue(REL_ATTR), NOOPENER_VALUE);
  }
}
