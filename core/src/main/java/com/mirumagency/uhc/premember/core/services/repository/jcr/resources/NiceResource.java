package com.mirumagency.uhc.premember.core.services.repository.jcr.resources;

import static java.util.stream.StreamSupport.stream;
import static org.apache.http.util.Asserts.notNull;

import com.day.cq.commons.jcr.JcrConstants;
import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.exceptions.FailedToAdaptResourceException;
import com.mirumagency.uhc.premember.core.exceptions.RequiredResourceNotFoundException;
import com.mirumagency.uhc.premember.core.util.BooleanUtil;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class NiceResource {

  private final Resource resource;

  public static NiceResource of(Resource resource) {
    notNull(resource, "resource");
    return new NiceResource(resource);
  }

  private NiceResource(Resource resource) {
    this.resource = resource;
  }

  public ValueMap getValueMap() {
    return resource.getValueMap();
  }


  public Boolean getBoolean(String key) {
    return Optional.ofNullable(resource.getValueMap().get(key, Boolean.class))
        .orElse(false);
  }

  public String getString(String key) {
    return Optional.ofNullable(resource.getValueMap().get(key, String.class))
        .orElse("");
  }

  public Double getDouble(String key) {
    return Optional.ofNullable(resource.getValueMap().get(key, Double.class))
            .orElse(0d);
  }

  public List<String> getList(String key) {
    return Optional.ofNullable(resource.getValueMap().get(key, new String[0]))
        .map(Arrays::asList)
        .orElse(ImmutableList.of());
  }

  public boolean hasValue(String key) {
    return Optional.ofNullable(resource.getValueMap().get(key, String.class)).isPresent();
  }

  public String getStringOrElse(String key, String value) {
    return Optional.ofNullable(resource.getValueMap().get(key, String.class))
        .orElse(value);
  }

  public ZonedDateTime getDate(String key) {
    return Optional.ofNullable(resource.getValueMap().get(key, Date.class))
        .map(date -> date.toInstant().atZone(ZoneId.systemDefault()))
        .orElse(null);
  }

  public Optional<NiceResource> getChild(String resourceName) {
    notNull(resourceName, "resourceName");
    return Optional.ofNullable(resource.getChild(resourceName))
        .map(NiceResource::of);
  }

  public NiceResource getChildOrThrow(String resourceName) {
    notNull(resourceName, "resourceName");
    return Optional.ofNullable(resource.getChild(resourceName))
        .map(NiceResource::of)
        .orElseThrow(() -> new RequiredResourceNotFoundException(
            "Required resource was not found: " + resource.getPath() + "/" + resourceName));
  }

  public NiceResource getContentOrThrow() {
    return getChildOrThrow(JcrConstants.JCR_CONTENT);
  }

  public Stream<NiceResource> getChildren() {
    return stream(resource.getChildren().spliterator(), false)
        .map(NiceResource::of);
  }

  public boolean isResourceType(String resourceType) {
    notNull(resourceType, "resourceType");
    return resource.isResourceType(resourceType);
  }

  public Resource getRawResource() {
    return resource;
  }

  public NiceResource getParentOrThrow() {
    return Optional.ofNullable(resource.getParent())
        .map(NiceResource::of)
        .orElseThrow(() -> new RequiredResourceNotFoundException(
            "Required parent not found for resource: " + resource.getPath()));
  }

  public <T> T adaptTo(Class<T> clazz) {
    T t = resource.adaptTo(clazz);
    return Optional.ofNullable(resource.adaptTo(clazz))
        .orElseThrow(() -> new FailedToAdaptResourceException(
            "Failed to adapt resource resource: " + resource.getPath() + " to class: " + clazz
                .getCanonicalName()));
  }

  public String getResourceType() {
    return resource.getResourceType();
  }

  public String getId() {
    String resourceType = resource.getResourceType();
    String prefix = resourceType.substring(resourceType.lastIndexOf("/") + 1);
    return prefix + "-" + Math.abs(resource.getPath().hashCode() - 1);
  }

  public boolean isTrue(String key) {
    return BooleanUtil.isTrue(this.getString(key));
  }

  public String getName(){
    return resource.getName();
  }
}
