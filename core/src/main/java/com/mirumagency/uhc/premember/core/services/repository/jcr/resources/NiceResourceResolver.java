package com.mirumagency.uhc.premember.core.services.repository.jcr.resources;

import static org.apache.http.util.Asserts.notNull;

import com.day.cq.wcm.api.PageManager;
import com.mirumagency.uhc.premember.core.exceptions.RequiredResourceNotFoundException;
import java.io.Closeable;
import java.util.Optional;
import org.apache.sling.api.resource.ResourceResolver;

public class NiceResourceResolver implements Closeable {

  private final ResourceResolver resourceResolver;

  public static NiceResourceResolver of(ResourceResolver serviceResourceResolver) {
    notNull(serviceResourceResolver, "serviceResourceResolver");
    return new NiceResourceResolver(serviceResourceResolver);
  }

  private NiceResourceResolver(ResourceResolver resourceResolver) {
    this.resourceResolver = resourceResolver;
  }

  @Override
  public void close() {
    resourceResolver.close();
  }

  public NiceResource getResourceOrThrow(String path) {
    notNull(path, "path");
    return Optional.ofNullable(resourceResolver.getResource(path))
        .map(NiceResource::of)
        .orElseThrow(() -> new RequiredResourceNotFoundException(
            "Required resource was not found: " + path));
  }

  public Optional<NiceResource> getResource(String path) {
    notNull(path, "path");
    return Optional.ofNullable(resourceResolver.getResource(path))
        .map(NiceResource::of);
  }

  public Optional<PageManager> getPageManager(){
     return Optional.ofNullable(resourceResolver.adaptTo(PageManager.class));
  }

  public String map(String url) {
    return resourceResolver.map(url);
  }
}
