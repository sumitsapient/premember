package com.mirumagency.uhc.premember.core.models.analytics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirumagency.uhc.premember.core.models.TokensForEmployer;
import com.mirumagency.uhc.premember.core.util.AnalyticsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Slf4j
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = DataLayerModel.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DataLayerModel extends TokensForEmployer {

  private ObjectMapper objectMapper;

  @ValueMapValue
  @Default(values = "")
  private String pageName;

  @ValueMapValue
  @Default(values = "")
  private String pageCategory;

  @ValueMapValue
  @Default(values = "")
  private String planName;

  @ValueMapValue
  @Default(values = "")
  private String compareNames;

  @PostConstruct
  public void init() {
    this.objectMapper = new ObjectMapper();
    this.pageName = normalize(tokenizeInViewMode(pageName));
    this.pageCategory = normalize(tokenizeInViewMode(pageCategory));
    this.planName = normalize(tokenizeInViewMode(planName));
    this.compareNames = normalize(tokenizeInViewMode(compareNames));
  }

  private String normalize(String text) {
    return AnalyticsUtil.normalize(text);
  }

  public String dataLayer() {
    String result = "";
    try {
      result = objectMapper.writeValueAsString(data());
    } catch (JsonProcessingException e) {
      log.error("Failed to create data layer", e);
    }
    return result;
  }

  private DigitalData data() {
    return new DigitalData(new Page(pageName, pageCategory), new Plan(planName, compareNames));
  }
}
