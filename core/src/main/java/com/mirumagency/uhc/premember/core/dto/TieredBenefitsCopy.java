package com.mirumagency.uhc.premember.core.dto;

import java.util.HashMap;
import java.util.Map.Entry;
import lombok.Getter;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;

@Getter
public class TieredBenefitsCopy {

  private String text;

  private String value;

  TieredBenefitsCopy(String text, String value) {

    this.text = text;
    this.value = value;
  }

  public static TieredBenefitsCopy emptyOption(String text) {
    return new TieredBenefitsCopy(text, "");
  }

  public static TieredBenefitsCopy ofContentFragmentEntry(Entry entry) {
    return new TieredBenefitsCopy(entry.getValue().toString(), String.format("{%s}", entry.getKey().toString()));
  }

  public ValueMap asValueMap() {
    ValueMap valueMap = new ValueMapDecorator(new HashMap<>());
    valueMap.put("text", text);
    valueMap.put("value", value);

    return valueMap;
  }
}
