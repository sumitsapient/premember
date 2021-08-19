package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Getter;

@Getter
public class HidePlanOptions {

  private static final String DISPLAY_TOGGLE_PROP = "hidePlanOptions";
  private static final String PLAN_OPTIONS_TITLE = "planOptionsTitle";

  private final boolean hidePlanOptions;
  private final String planOptionstitle;

  public HidePlanOptions(boolean hidePlanOptions, String planOptionsTitle) {
    this.hidePlanOptions = hidePlanOptions;
    this.planOptionstitle = planOptionsTitle;
  }

  public static HidePlanOptions of(NiceResource employerData) {
    return new HidePlanOptions(employerData.getBoolean(DISPLAY_TOGGLE_PROP),employerData.getString(PLAN_OPTIONS_TITLE));
  }
}
