package com.mirumagency.uhc.premember.core.services;

import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.osgi.service.component.annotations.Component;

@Component(
    immediate = true,
    service = TransformerFactory.class,
    property = {"pipeline.type=target-blank-link-transformer"})
public class TargetBlankLinkTransformerFactory implements TransformerFactory {
  @Override
  public Transformer createTransformer() {
    return new TargetBlankLinkTransformer();
  }
}
