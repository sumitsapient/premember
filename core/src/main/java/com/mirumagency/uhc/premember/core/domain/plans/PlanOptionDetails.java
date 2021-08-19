package com.mirumagency.uhc.premember.core.domain.plans;

import static com.mirumagency.uhc.premember.core.converters.TokenizedTextConverter.firstTokenName;
import static com.mirumagency.uhc.premember.core.converters.TokenizedTextConverter.hasTokens;
import static com.mirumagency.uhc.premember.core.util.MapUtil.mapKey;
import static com.mirumagency.uhc.premember.core.util.MapUtil.mapValue;
import static com.mirumagency.uhc.premember.core.util.MapUtil.mapValuesToStrings;
import static com.mirumagency.uhc.premember.core.util.MapUtil.toMap;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.WithData;
import com.mirumagency.uhc.premember.core.util.MapUtil;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = "tokens")
public class PlanOptionDetails implements WithData {

  private static final String COPY_SUFFIX = ".copy";

  protected final Map<String, Object> data;
  protected final Map<String, String> tokens;
  private final PlanCopy copy;

  public static PlanOptionDetails empty() {
    return new PlanOptionDetails(PlanCopy.empty(), ImmutableMap.of());
  }

  public static PlanOptionDetails demo(PlanType type) {
    return new PlanOptionDetails(PlanCopy.empty(),
        ImmutableMap.of("planName", "Demo " + type.getTypeName() + " Plan Option"));
  }

  public PlanOptionDetails(PlanCopy copy, Map<String, Object> data) {
    this.data = resolveData(copy, data);
    this.tokens = mapValuesToStrings(this.data);
    this.copy = copy;
  }

  private Map<String, Object> resolveData(PlanCopy copy, Map<String, Object> data) {
    Map<String, Object> resolvedBooleansUsingCopy = resolveBooleansUsingCopy(copy, data);
    Map<String, Object> resolveTokensUsingCopy = resolveTokensUsingCopy(copy, data);

    return ImmutableMap.<String, Object>builder()
        .putAll(data)
        .putAll(resolvedBooleansUsingCopy)
        .putAll(resolveTokensUsingCopy)
        .build();
  }

  private Map<String, Object> resolveTokensUsingCopy(PlanCopy copy, Map<String, Object> data) {
    return data.entrySet().stream()
        .filter(e -> hasTokens(e.getValue().toString()))
        .map(e -> mapValue(e, value -> firstTokenName(e.getValue().toString())))
        .map(e -> mapValue(e, token -> copy.getData().getOrDefault(token, "")))
        .map(e -> mapKey(e, this::addCopySuffix))
        .collect(toMap());
  }

  private Map<String, Object> resolveBooleansUsingCopy(PlanCopy copy, Map<String, Object> data) {
    return data.entrySet().stream()
        .filter(e -> copy.contains(e.getKey()))
        .filter(MapUtil::valueIsABoolean)
        .map(e -> mapValue(e, value -> MapUtil.valueIsATrueBoolean(e) ? copy.get(e.getKey()) : ""))
        .map(e -> mapKey(e, this::addCopySuffix))
        .collect(toMap());
  }

  private String addCopySuffix(String key) {
    return key + COPY_SUFFIX;
  }
}
