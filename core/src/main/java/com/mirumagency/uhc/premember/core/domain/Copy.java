package com.mirumagency.uhc.premember.core.domain;

import static com.mirumagency.uhc.premember.core.util.MapUtil.mapValue;
import static com.mirumagency.uhc.premember.core.util.MapUtil.toMap;
import static java.util.Collections.emptyMap;

import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Copy implements WithData {

  private Map<String, Object> data;

  public Copy(Map<String, Object> data) {
    this.data = data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public Map<String, String> tokens() {
    return data.entrySet().stream()
        .map(e -> mapValue(e, Object::toString))
        .collect(toMap());
  }

  public static Copy empty() {
    return new Copy(emptyMap()) {
    };
  }
}
