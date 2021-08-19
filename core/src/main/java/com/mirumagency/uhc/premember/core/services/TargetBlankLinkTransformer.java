package com.mirumagency.uhc.premember.core.services;

import org.apache.sling.rewriter.DefaultTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TargetBlankLinkTransformer extends DefaultTransformer {

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws SAXException {
    if ("a".equals(localName) && hasTargetBlank(attributes)) {
      SafeRelAttributes attrs = new SafeRelAttributes(attributes);
      super.startElement(uri, localName, qName, attrs.getSafeAttrs());
    } else {
      super.startElement(uri, localName, qName, attributes);
    }
  }

  private boolean hasTargetBlank(Attributes attributes) {
    return "_blank".equals(attributes.getValue("target"));
  }
}
